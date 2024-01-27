package com.nuri.api.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.nuri.api.service.ShoppingServiceImpl;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.nuri.api.model.CartVO;
import com.nuri.api.model.CateVO;
import com.nuri.api.model.EventVO;
import com.nuri.api.model.PayVO;
import com.nuri.api.model.UserVO;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value="/shopping")
public class ApiController {
	@Autowired
	ShoppingServiceImpl service;
	
	private Gson gson = new Gson();
	
	/**
     * 회원가입 처리
     * @param userVO
     * @return
     */
	@ApiOperation(value="회원가입 처리", notes="user table에 고객정보 insert")
    @RequestMapping(value="/signup", method=RequestMethod.POST)
	@ResponseBody
    public String userInsert(UserVO userVO) {
        System.out.println("UserVO: " + userVO);
        int cnt = service.userInsert(userVO);
        
        System.out.println("회원가입 성공여부: " + cnt);
        return gson.toJson(cnt);
    }
	
	/**
     * 로그인 처리
     * @param user_id
     * @param user_passwd
     * @return
     */
	@ApiOperation(value="로그인", notes="로그인처리")
    @RequestMapping(value="/login", method=RequestMethod.POST)
    @ResponseBody
    public String login(String user_id, String user_passwd) {
        
        //System.out.println("로그인진입");
        int cnt = service.login(user_id, user_passwd);
        
        UserVO userVO = new UserVO();
        if(cnt == 1) {
            userVO = service.readById(user_id);
            System.out.println("로그인성공");
        } 
        
        return gson.toJson(cnt);
    }
    
    /**
     * 로그아웃 처리
     * @param session
     * @return
     */
    @ApiOperation(value="로그아웃", notes="로그아웃 처리")
    @RequestMapping(value="/logout", method=RequestMethod.GET)
    @ResponseBody
    public ModelAndView logout(HttpSession session) {
        //System.out.println("로그아웃진입");
        ModelAndView mav = new ModelAndView();
        session.invalidate();
        mav.setViewName("redirect:/");
        //System.out.println("로그아웃성공");
        return mav;
    }
    
    /**
     * 카테고리별 상품 불러오기
     * @param category_idx
     * @return
     */
	@ApiOperation(value="상품 가져오기", notes="카테고리에 해당하는 전체 상품을 가져온다")
	@RequestMapping(value="/getproducts", method = RequestMethod.GET)
	@ResponseBody
	public String getProducts(@RequestParam String category_idx) {
	    List<CateVO> cate_list = service.getProducts(Integer.valueOf(category_idx));
		System.out.println("cate_list 도착: " + cate_list);
		
		return gson.toJson(cate_list);
	}
	
	/**
     * 상품검색
     * @param cate_name
     * @return
     */
	@ApiOperation(value="상품검색", notes="검색어에 해당하는 단어를 가져온다")
    @RequestMapping(value="/getsearch", method = RequestMethod.GET)
    public @ResponseBody String getSearch(@RequestParam String cate_name) {
        
        System.out.println("getsearch 진입");
        System.out.println("cate_name: " + cate_name);
        
        List<CateVO> cate_cate_list = service.getSearch(cate_name);
        System.out.println("cate_cate_list: " + cate_cate_list);
        
        
        return gson.toJson(cate_cate_list);
    }
	
	/**
     * 상품 상세페이지
     * @param cate_idx
     * @return
     */
	@ApiOperation(value="상품 상세페이지", notes="클릭 한 상품에 해당하는 상세페이지로 이동")
    @RequestMapping(value="/getdetail", method = RequestMethod.GET)
    public String getDetail(@RequestParam String cate_idx) {
        
        //System.out.println("session.getAttribute(\"user_idx\"): " + session.getAttribute("user_idx"));
        
        CateVO detail = service.getDetail(Integer.valueOf(cate_idx));
        System.out.println("api detail:" + detail);
        
        return gson.toJson(detail);
    }
	
	/**
     * 장바구니 추가
     * @param model
     * @param cate_idx
     * @return
     */
	@ApiOperation(value="장바구니 추가", notes="클릭 한 상품을 장바구니에 추가")
    @RequestMapping(value="/addcart", method = RequestMethod.POST)
    public @ResponseBody String addCart(
            @JsonProperty CartVO cartVO
            ) {

        Gson gson = new Gson();
        int cnt = service.addCart(cartVO);
        System.out.println("cartVO: " + cartVO);
        
        return gson.toJson(cnt);
    }
	
	/**
	 * 구매하기 클릭 시 구매테이블에 저장
	 * @param user_id
	 * @return
	 */
	@ApiOperation(value="구매테이블에저장", notes="구매하기 클릭 시 구매테이블에 저장")
	@RequestMapping(value="/addpay", method = RequestMethod.GET)
	@ResponseBody
	public String goPay(String user_id) {
	  List<CartVO> cart_list = service.selectPay(user_id);
	  System.out.println("cart_list:"  + cart_list);
	  List<PayVO> pay_add = new ArrayList<PayVO>();
	  int cnt = 0;
	  PayVO payVO = new PayVO();
	  for(int i=0; i<cart_list.size(); i++) {
	    payVO.setCate_img(cart_list.get(i).getCart_img());
	    payVO.setUser_id(cart_list.get(i).getUser_id());
	    payVO.setCate_name(cart_list.get(i).getCart_name());
	    payVO.setCate_price(cart_list.get(i).getCart_price());
	    cnt = service.addPay(payVO);
	  }
	   
	  return gson.toJson(cnt);
	}
	
	/**
	 * 구매내역 삭제
	 * @param user_id
	 * @return
	 */
	@ApiOperation(value="구매내역삭제", notes="구매한경우 장바구니 내역 삭제")
    @RequestMapping(value="/deletecart", method = RequestMethod.GET)
    @ResponseBody
	public String deleteCart(String user_id) {
	  System.out.println("userid: " + user_id);
	  int cnt = service.deleteProduct(user_id);
	  System.out.println("장바구니 내역 삭제");
	  return gson.toJson(cnt);
	}
    
	/**
     * 장바구니 리스트 불러오기
     * @param model
     * @param user_idx
     * @return
     */
	@ApiOperation(value="장바구니 리스트", notes="회원별 장바구니 리스트 불러오기")
    @RequestMapping(value="/getcartlist", method = RequestMethod.GET)
    public String getCartList(String user_id) {
        
        //System.out.println("진입: " + user_idx);
        UserVO userInfo = service.getUserInfo(user_id);
        
        List<CartVO> cart_list = service.getCartList(user_id);
        
        System.out.println("장바구니 리스트 cart_list: " + cart_list);
        
        return gson.toJson(cart_list);
    }
	
	
	/**
     * 낮은가격순으로 정렬
     * @param categrp_idx
     * @return
     */
	@ApiOperation(value="낮은가격순으로 정렬", notes="낮은가격순으로 정렬")
    @RequestMapping(value="/priceasc", method = RequestMethod.GET)
    public @ResponseBody String orderByCatePriceAsc(int category_idx) {
        
        //System.out.println("priceasc 진입");
        //System.out.println("category_idx: " + category_idx);
        
        Gson gson = new Gson();
        
        
        List<CateVO> asc_list = service.orderByCatePriceAsc(category_idx);
        
        return gson.toJson(asc_list);
    }
	
	/**
     * 높은가격순으로 정렬
     * @param categrp_idx
     * @return
     */
	@ApiOperation(value="높은가격순으로 정렬", notes="높은가격순으로 정렬")
    @RequestMapping(value="/pricedesc", method = RequestMethod.GET)
    public @ResponseBody String orderByCatePriceDesc(int category_idx) {
        
        //System.out.println("pricedesc 진입");
        //System.out.println("category_idx: " + category_idx);
        
        Gson gson = new Gson();
        
        List<CateVO> desc_list = service.orderByCatePriceDesc(category_idx);
        System.out.println("높은 가격순으로 정렬:" + desc_list);
        
        return gson.toJson(desc_list);
    }
	
	/**
     * 최신순으로 정렬
     * @param categrp_idx
     * @return
     */
	@ApiOperation(value="최신순으로 정렬", notes="최신순으로 정렬")
    @RequestMapping(value="/orderbydate", method = RequestMethod.GET)
    public @ResponseBody String orderByDate(int category_idx) {
        
        //System.out.println("orderbydate 진입");
        //System.out.println("category_idx: " + category_idx);
        
        Gson gson = new Gson();
        
        
        List<CateVO> date_list = service.orderByDate(category_idx);
        System.out.println("최신순으로 정렬: " + date_list);
        
        return gson.toJson(date_list);
    }
	
	
	
	/**
     * 마이페이지(회원구매내역)로 이동
     * @param user_idx
     * @return
     */
	@ApiOperation(value="마이페이지", notes="마이페이지(회원구매내역)로 이동")
    @RequestMapping(value="/mypage", method = RequestMethod.GET)
    public String goMyPage(String user_id) {
        
        System.out.println("마이페이지 진입");
        List<PayVO> my_list = service.goMyPage(user_id);
        
        UserVO userInfo = service.getUserInfo(user_id);
        
        System.out.println("my_list: " + my_list);
        
        return gson.toJson(my_list);
    }
	
	/**
     * 이벤트페이지 이동
     * @param model
     * @param event_idx
     * @return
     */
	@ApiOperation(value="이벤트페이지", notes="이벤트페이지로 이동")
    @RequestMapping(value="/goevent", method = RequestMethod.GET)
	@ResponseBody
    public String goEvent(int event_idx) {
        
	  System.out.println("event_idx: " + event_idx);
        EventVO eventVO = service.goEvent(event_idx);
        System.out.println("evnetvo: "+ eventVO);
        return gson.toJson(eventVO);
    }
	
	
	/**
	 * 고객정보조회
	 * @param user_id
	 * @return
	 */
	@ApiOperation(value="고객정보조회", notes="아이디에 해당하는 고객정보 조회")
	@RequestMapping(value="/getUserInfo", method = RequestMethod.GET)
	public String getUserInfo(String user_id) {
	  UserVO userInfo = service.readById(user_id);
	  
	  System.out.println("userInfo: " + userInfo);
	  System.out.println("고객정보 확인완료");
	  
	  return gson.toJson(userInfo);
	}
}
