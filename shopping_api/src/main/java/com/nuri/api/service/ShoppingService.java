package com.nuri.api.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nuri.api.model.CartVO;
import com.nuri.api.model.CateVO;
import com.nuri.api.model.EventVO;
import com.nuri.api.model.PayVO;
import com.nuri.api.model.UserVO;

public interface ShoppingService {
	/**
	 * USER INSERT
	 * @param userVO
	 * @return
	 */
	public int userInsert(UserVO userVO);
	
	/**
	 * 아이디 중복확인
	 * @param user_id
	 * @return
	 */
	public int checkID(String user_id);
	
	/**
	 * 로그인 처리
	 * @param user_id
	 * @param user_passwd
	 * @return
	 */
	public int login(String user_id, String user_passwd);
	
	/**
	 * user_id에 해당하는 회원정보 불러오기
	 * @param user_id
	 * @return
	 */
	public UserVO readById(String user_id);
	
	/**
	 * category_idx에 해당하는 리스트 불러오기
	 * @param category_idx
	 * @return
	 */
	public List<CateVO> getProducts(int category_idx);
	
	/**
	 * 상품검색
	 * @param cate_name
	 * @return
	 */
	public List<CateVO> getSearch(String cate_name);
	
	/**
	 * 상품 상세페이지
	 * @param cate_idx
	 * @return
	 */
	public CateVO getDetail(int cate_idx);
	
	/**
	 * 장바구니 담기
	 * @param cate_idx
	 * @return
	 */
	public int addCart(CartVO cartVO);
	
	/**
	 * 장바구니리스트
	 * @param user_idx
	 * @return
	 */
	public List<CartVO> getCartList(String user_id);
	
	/**
	 * 낮은 가격순 정렬
	 * @param category_idx
	 * @return
	 */
	public List<CateVO> orderByCatePriceAsc(int category_idx);
	
	/**
	 * 높은 가격순 정렬
	 * @param category_idx
	 * @return
	 */
	public List<CateVO> orderByCatePriceDesc(int category_idx);
	
	/**
	 * 최신 날짜순 정렬
	 * @param category_idx
	 * @return
	 */
	public List<CateVO> orderByDate(int category_idx);
	
	/**
	 * 회원별 정보 조회
	 * @param user_idx
	 * @return
	 */
	public UserVO getUserInfo(String user_id);
	
	/**
	 * 구매하기 클릭시 구매테이블에 저장
	 * @param payVO
	 * @return
	 */
	public int addPay(PayVO payVO);
	
	/**
	 * 마이페이지
	 * @param user_idx
	 * @return
	 */
	public List<PayVO> goMyPage(String user_id);
	
	/**
	 * 이벤트 페이지 이동
	 * @param event_idx
	 * @return
	 */
	public EventVO goEvent(int event_idx);
	
	/**
	 * 구매한 내역 삭제
	 * @param cart_idx
	 * @param user_id
	 * @return
	 */
	public int deleteProduct(String user_id);
	
    
    /**
     * 고객id별 카트에 담긴 내역 불러오기
     * @param user_id
     * @return
     */
	public List<CartVO> selectPay(String user_id);
		
}
