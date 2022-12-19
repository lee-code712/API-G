package com.digital.v3.route;

import static com.digital.v3.utils.HttpConnectionUtils.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.digital.v3.exception.ResErrorCodeException;
import com.digital.v3.schema.ErrorMsg;
import com.digital.v3.schema.Product;
import com.digital.v3.schema.ProductList;
import com.digital.v3.utils.ExceptionUtils;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "상품", description = "Product Related API")
@RequestMapping(value = "/rest/product")
public class ProductRoute {
	
	private static final String baseUrlKey = "Product";
	
	@RequestMapping(value = "/write", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "상품 등록", notes = "상품 등록을 위한 API. *입력 필드: categoryId, productName, price")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = Product.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> productWrite (@ApiParam(value = "상품 정보", required = true) @RequestBody Product product,
			HttpServletRequest request) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		
		try {
			String token = request.getHeader("Authorization");
			
			String responseJson = requestPOST(baseUrlKey, "/rest/product/write", product, token); 
			Product resProduct = (Product) jsonToObject(responseJson, Product.class);
			
			return new ResponseEntity<Product>(resProduct, header, HttpStatus.valueOf(200));
		} catch (ResErrorCodeException e) {
			return new ResponseEntity<String>(e.getMessage(), header, HttpStatus.valueOf(500));
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
	}

	@RequestMapping(value = "/inquiry/{keyword}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "상품 검색", notes = "키워드를 포함하는 상품명으로 상품 목록을 검색하는 API.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = ProductList.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> productSearchBykeyword (@ApiParam(value = "상품명 키워드", required = true) @PathVariable String keyword,
			HttpServletRequest request) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		
		try {
			String token = request.getHeader("Authorization");
			
			String responseJson = requestGET(baseUrlKey, "/rest/product/inquiry/" + encodeParams(keyword), token); 
			ProductList resProducts = (ProductList) jsonToObject(responseJson, ProductList.class);
			
			return new ResponseEntity<ProductList>(resProducts, header, HttpStatus.valueOf(200));
		} catch (ResErrorCodeException e) {
			return new ResponseEntity<String>(e.getMessage(), header, HttpStatus.valueOf(500));
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
	}
	
	@RequestMapping(value = "/inquiry/byCategory/{categoryName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "카테고리별 상품 검색", notes = "특정 카테고리의 상품 목록을 검색하는 API.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = ProductList.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> productSearchByCategory (@ApiParam(value = "카테고리명", required = true) @PathVariable("categoryName") String categoryName,
			HttpServletRequest request) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		
		try {
			String token = request.getHeader("Authorization");
			
			String responseJson = requestGET(baseUrlKey, "/rest/product/inquiry/byCategory/" + encodeParams(categoryName), token); 
			ProductList resProducts = (ProductList) jsonToObject(responseJson, ProductList.class);
			
			return new ResponseEntity<ProductList>(resProducts, header, HttpStatus.valueOf(200));
		} catch (ResErrorCodeException e) {
			return new ResponseEntity<String>(e.getMessage(), header, HttpStatus.valueOf(500));
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
	}
	
}
