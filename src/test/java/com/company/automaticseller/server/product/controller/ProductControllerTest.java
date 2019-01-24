package com.company.automaticseller.server.product.controller;

import com.company.automaticseller.server.product.repository.ProductRepository;
import com.company.automaticseller.server.product.entity.Product;
import lombok.val;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductControllerTest {

	@Mock
	private ProductRepository productRepository;

	ProductController testedObj;

	@Before
	public void setUp() {
		testedObj = new ProductController(productRepository);
	}

	@Test
	public void shouldFindProductWithGivenArticleIdIfExistsInDatabase() {
		val p = new Product.ProductBuilder(123, "SomeName", 1.12F)
				.build();
		when(productRepository.findByArticleId(anyLong())).thenReturn(p);

		val result = testedObj.findProduct(123);

		assertThat(result).isEqualTo(p);
	}

	@Test
	public void shouldReturnNullIfCouldNotFindProductInDatabase() {
		when(productRepository.findByArticleId(anyLong())).thenReturn(null);

		val result = testedObj.findProduct(123);

		assertThat(result).isEqualTo(null);
	}

	@Test
	public void shouldRetrieveAllProductsFromDatabase() {
		val productList = Lists.newArrayList(
				new Product.ProductBuilder(123, "Name1", 1.12F).build(),
				new Product.ProductBuilder(124, "Name2", 1.23F).build()
		);
		when(productRepository.findAll()).thenReturn(productList);

		val result = testedObj.findAllProduct();

		assertThat(result).hasSize(2);
		assertThat(result).containsAll(productList);
	}

	@Test
	public void shouldAddProductToDatabaseIfDataIsCorrect() {
		ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
		doReturn(null).when(productRepository).save(captor.capture());

		val result = testedObj.addProduct(123L, "Name", "Some", 1.39F, 1);

		val p = captor.getValue();
		assertThat(p.getArticleId()).isEqualTo(123L);
		assertThat(p.getName()).isEqualTo("Name");
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo("Product has been added");
	}

	@Test
	public void shouldDeleteProductIfExistsInDatabase() {
		val captor = ArgumentCaptor.forClass(Product.class);
		val p = new Product.ProductBuilder(123L, "Name", 1.12F).build();
		when(productRepository.findByArticleId(anyLong())).thenReturn(p);
		doNothing().when(productRepository).delete(captor.capture());

		val result = testedObj.deleteProduct(123L);

		assertThat(captor.getValue()).isEqualTo(p);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo("Product has been deleted");
	}

	@Test
	public void shouldNotDeleteProductIfNotFoundInDatabase() {
		when(productRepository.findByArticleId(anyLong())).thenReturn(null);

		val result = testedObj.deleteProduct(111L);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo("Product has not been found in database");
	}

	@Test
	public void shouldUpdatePriceOfProductInDatabase() {
		val captor = ArgumentCaptor.forClass(Product.class);
		val p = new Product.ProductBuilder(123L, "Name", 1.12F).build();
		when(productRepository.findByArticleId(anyLong())).thenReturn(p);
		doReturn(null).when(productRepository).save(captor.capture());

		val result = testedObj.updatePriceOfProduct(123L, 2.5F);

		val product = captor.getValue();
		assertThat(product).isEqualTo(product);
		assertThat(product.getPrice()).isEqualTo(2.5F);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo("Price of product has been modified");
	}

	@Test
	public void shouldNotUpdatePriceOfProductInDatabase() {
		when(productRepository.findByArticleId(anyLong())).thenReturn(null);

		val result = testedObj.updatePriceOfProduct(111L, 1.12F);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo("Product has not been found in database");
	}

	@Test
	public void shouldUpdateQuantityOfProductInDatabase() {
		val captor = ArgumentCaptor.forClass(Product.class);
		val p = new Product.ProductBuilder(123L, "Name", 1.12F).build();
		when(productRepository.findByArticleId(anyLong())).thenReturn(p);
		doReturn(null).when(productRepository).save(captor.capture());

		val result = testedObj.updateQuantityOfProductInStorage(123L, 4);

		val product = captor.getValue();
		assertThat(product).isEqualTo(product);
		assertThat(product.getQuantity()).isEqualTo(4);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo("Quantity of product has been modified");
	}

	@Test
	public void shouldNotUpdateQuantityOfProductInDatabase() {
		when(productRepository.findByArticleId(anyLong())).thenReturn(null);

		val result = testedObj.updateQuantityOfProductInStorage(111L, 25);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo("Product has not been found in database");
	}
}