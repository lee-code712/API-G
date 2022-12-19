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
import com.digital.v3.schema.Category;
import com.digital.v3.schema.ErrorMsg;
import com.digital.v3.utils.ExceptionUtils;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "카테고리", description = "Category Related API")
@RequestMapping(value = "/rest/category")
public class CategoryRoute {

	private static final String baseUrlKey = "Product";

	@RequestMapping(value = "/write", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "카테고리 등록", notes = "카테고리 등록을 위한 API. *입력 필드: categoryName")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = Category.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> categoryWrite (@ApiParam(value = "카테고리 정보", required = true) @RequestBody Category category,
			HttpServletRequest request) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		
		try {
			String token = request.getHeader("Authorization");
			
			String responseJson = requestPOST(baseUrlKey, "/rest/category/write", category, token); 
			Category resCategory = (Category) jsonToObject(responseJson, Category.class);
			
			return new ResponseEntity<Category>(resCategory, header, HttpStatus.valueOf(200));
		} catch (ResErrorCodeException e) {
			return new ResponseEntity<String>(e.getMessage(), header, HttpStatus.valueOf(500));
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
	}
	
	@RequestMapping(value = "/inquiry/{categoryName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "카테고리 검색", notes = "카테고리명으로 카테고리 정보를 검색하는 API.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = Category.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> categorySearch (@ApiParam(value = "카테고리명", required = true) @PathVariable String categoryName,
			HttpServletRequest request) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		
		try {
			String token = request.getHeader("Authorization");
			
			String responseJson = requestGET(baseUrlKey, "/rest/category/inquiry/" + encodeParams(categoryName), token); 
			Category resCategory = (Category) jsonToObject(responseJson, Category.class);
			
			return new ResponseEntity<Category>(resCategory, header, HttpStatus.valueOf(200));
		} catch (ResErrorCodeException e) {
			return new ResponseEntity<String>(e.getMessage(), header, HttpStatus.valueOf(500));
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
	}
	
}
