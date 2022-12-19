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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.digital.v3.exception.ResErrorCodeException;
import com.digital.v3.schema.ErrorMsg;
import com.digital.v3.schema.Inventory;
import com.digital.v3.schema.SuccessMsg;
import com.digital.v3.utils.ExceptionUtils;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "재고", description = "Inventory Related API")
@RequestMapping(value = "/rest/inventory")
public class InventoryRoute {

	private static final String baseUrlKey = "Inventory";
	
	@RequestMapping(value = "/write", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "재고 등록", notes = "특정 상품의 재고를 등록하기 위한 API. *입력 필드: productId, quantity")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = Inventory.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> inventoryWrite (@ApiParam(value = "재고 정보", required = true) @RequestBody Inventory inventory,
			HttpServletRequest request) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		
		try {
			String token = request.getHeader("Authorization");
			
			String responseJson = requestPOST(baseUrlKey, "/rest/inventory/write", inventory, token); 
			Inventory resInventory = (Inventory) jsonToObject(responseJson, Inventory.class);
			
			return new ResponseEntity<Inventory>(resInventory, header, HttpStatus.valueOf(200));
		} catch (ResErrorCodeException e) {
			return new ResponseEntity<String>(e.getMessage(), header, HttpStatus.valueOf(500));
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
	}
	
	@RequestMapping(value = "/inquiry/{productName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "재고 검색", notes = "상품명으로 상품 재고를 검색하는 API.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = Inventory.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> inventorySearch (@ApiParam(value = "상품명", required = true) @PathVariable String productName,
			HttpServletRequest request) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		
		try {
			String token = request.getHeader("Authorization");
			
			String responseJson = requestGET(baseUrlKey, "/rest/inventory/inquiry/" + encodeParams(productName), token); 
			Inventory resInventory = (Inventory) jsonToObject(responseJson, Inventory.class);
			
			return new ResponseEntity<Inventory>(resInventory, header, HttpStatus.valueOf(200));
		} catch (ResErrorCodeException e) {
			return new ResponseEntity<String>(e.getMessage(), header, HttpStatus.valueOf(500));
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
	}
	
	@RequestMapping(value = "/inquiry/byId/{productId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "재고 검색", notes = "상품 ID로 상품 재고를 검색하는 API.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = Inventory.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> inventorySearchById (@ApiParam(value = "상품 ID", required = true, example = "0") @PathVariable long productId,
			HttpServletRequest request) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		
		try {
			String token = request.getHeader("Authorization");
			
			String responseJson = requestGET(baseUrlKey, "/rest/inventory/inquiry/byId/" + productId, token); 
			Inventory resInventory = (Inventory) jsonToObject(responseJson, Inventory.class);
			
			return new ResponseEntity<Inventory>(resInventory, header, HttpStatus.valueOf(200));
		} catch (ResErrorCodeException e) {
			return new ResponseEntity<String>(e.getMessage(), header, HttpStatus.valueOf(500));
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
	}
	
	@RequestMapping(value = "/quantityCheck", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "재고 수량 검증", notes = "재고 수량이 유효한지 검증하는 API.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = Inventory.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> quantityCheck (@ApiParam(value = "상품 ID", required = true, example = "0") @RequestParam long productId, 
			@ApiParam(value = "상품 수량", required = true, example = "0") @RequestParam long quantity, HttpServletRequest request) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		
		try {
			String token = request.getHeader("Authorization");
			
			String params = "?productId=" + productId + "&quantity=" + quantity;
			String responseJson = requestGET(baseUrlKey, "/rest/inventory/quantityCheck" + params, token); 
			Inventory resInventory = (Inventory) jsonToObject(responseJson, Inventory.class);
			
			return new ResponseEntity<Inventory>(resInventory, header, HttpStatus.valueOf(200));
		} catch (ResErrorCodeException e) {
			return new ResponseEntity<String>(e.getMessage(), header, HttpStatus.valueOf(500));
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "재고 변경", notes = "특정 상품의 재고 수량을 변경하는 API. *입력 필드: productId, quantity")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = SuccessMsg.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> inventoryUpdate (@ApiParam(value = "재고 정보", required = true) @RequestBody Inventory inventory,
			HttpServletRequest request) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		
		try {
			String token = request.getHeader("Authorization");
			
			String responseJson = requestPOST(baseUrlKey, "/rest/inventory/update", inventory, token); 
			SuccessMsg resSuccess = (SuccessMsg) jsonToObject(responseJson, SuccessMsg.class);
			
			return new ResponseEntity<SuccessMsg>(resSuccess, header, HttpStatus.valueOf(200));
		} catch (ResErrorCodeException e) {
			return new ResponseEntity<String>(e.getMessage(), header, HttpStatus.valueOf(500));
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
	}
}
