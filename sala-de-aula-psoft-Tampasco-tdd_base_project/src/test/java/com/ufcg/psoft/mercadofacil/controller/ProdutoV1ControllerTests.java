package com.ufcg.psoft.mercadofacil.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.ProdutoRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de Produtos")
public class ProdutoV1ControllerTests {
    @Autowired
    MockMvc driver;

    @Autowired
    ProdutoRepository<Produto, Long> produtoRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    Produto produto;

    @BeforeEach
    void setup() {
        produto = produtoRepository.find(10L);
    }

    @AfterEach
    void tearDown() {
        produto = null;
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de campos obrigatórios")
    class ProdutoValidacaoCamposObrigatorios {

        @Test
        @DisplayName("Quando alteramos o nome do produto com dados válidos")
        void quandoAlteramosNomeDoProdutoValido() throws Exception {
            // Arrange
            produto.setNome("Produto Dez Alterado");

            // Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Produto resultado = objectMapper.readValue(responseJsonString, Produto.ProdutoBuilder.class).build();

            // Assert
            assertEquals(resultado.getNome(), "Produto Dez Alterado");
        }

    }

    @Nested
    @DisplayName("Conjunto de casos de verificação da regra sobre o preço")
    class ProdutoValidacaoRegrasDoPreco {
       
        @Test
        @DisplayName("Quando alteramos o preco do produto com dados válidos")
        void quandoAlteramoPrecoDoProdutoValido() throws Exception {
            // Arrange
            produto.setPreco(3.25);

            // Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Produto resultado = objectMapper.readValue(responseJsonString, Produto.ProdutoBuilder.class).build();

            // Assert
            assertEquals(resultado.getPreco(), 3.25);
        }

        @Test
        @DisplayName("Quando alteramos o preco do produto com dados inválidos")
        void quandoAlteramoPrecoDoProdutoInvalido() throws Exception {
            // Arrange
            produto.setPreco(0.0);

                  // Act + Assert
                  assertThrows(RuntimeException.class,() -> driver.perform(put("/v1/produtos/" + produto.getId())
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(produto)))
          .andExpect(status().isOk())
          .andDo(print())
          .andReturn().getResponse().getContentAsString());
            
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação da validação do código de barras")
    class ProdutoValidacaoCodigoDeBarras {

        @Test
        @DisplayName("Quando alteramos o código de barras do produto com dados válidos")
        void quandoAlteramosBarrasDoProdutoValido() throws Exception {
            // Arrange
            produto.setCodigoBarra("7899137500104");

            // Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Produto resultado = objectMapper.readValue(responseJsonString, Produto.ProdutoBuilder.class).build();

            // Assert
            assertEquals(resultado.getCodigoBarra(), "7899137500104");
        }

        @Test
        @DisplayName("Quando alteramos o código de barras do produto com dados inválidos")
        void quandoAlteramosBarrasDoProdutoInvalido() throws Exception {
            // Arrange
            produto.setCodigoBarra("0000");

            // Act + Assert
            assertThrows(RuntimeException.class,() -> driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString());
        }



    }

}
