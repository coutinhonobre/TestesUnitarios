package com.github.coutinhonobre.servicos;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;

import com.github.coutinhonobre.builders.FilmeBuilder;
import com.github.coutinhonobre.builders.UsuarioBuilder;
import com.github.coutinhonobre.dados.LocacaoDAO;
import com.github.coutinhonobre.dados.LocacaoDAOFake;
import com.github.coutinhonobre.entidades.Filme;
import com.github.coutinhonobre.entidades.Locacao;
import com.github.coutinhonobre.entidades.Usuario;
import com.github.coutinhonobre.exceptions.FilmeSemEstoqueException;
import com.github.coutinhonobre.exceptions.LocadoraException;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {
	
	private LocacaoService service;
	
	private LocacaoDAO dao;
	
	private SPCService spc;
	
	@Parameter
	public List<Filme> filmes;
	
	@Parameter(value=1)
	public Double valorLocacao;
	
	@Parameter(value = 2)
	public String cenario;
	
	private static Filme filme1 = FilmeBuilder.umFilme().agora();
	private static Filme filme2 = FilmeBuilder.umFilme().agora();
	private static Filme filme3 = FilmeBuilder.umFilme().agora();
	private static Filme filme4 = FilmeBuilder.umFilme().agora();
	private static Filme filme5 = FilmeBuilder.umFilme().agora();
	private static Filme filme6 = FilmeBuilder.umFilme().agora();
	private static Filme filme7 = FilmeBuilder.umFilme().agora();
	
	@Before
	public void setup() {
		service = new LocacaoService();
		LocacaoDAO dao = Mockito.mock(LocacaoDAO.class);
		service.setLocacaoDAO(dao);
		spc = Mockito.mock(SPCService.class);
		service.setSPCService(spc);
	}
	
	
	//Test Data-driven
	@Parameters(name="{2}")
	public static Collection<Object[]> getParametros(){
		return Arrays.asList(new Object[][] {
			{Arrays.asList(filme1), 4.0, "1 Filmes: Sem Desconto"},
			{Arrays.asList(filme1, filme2), 8.0, "2 Filmes: Sem Desconto"}, 
			{Arrays.asList(filme1, filme2, filme3), 11.0, "3 Filmes: 25%"}, 
			{Arrays.asList(filme1, filme2, filme3, filme4), 13.0, "4 Filmes: 50%"}, 
			{Arrays.asList(filme1, filme2, filme3, filme4, filme5), 14.0, "5 Filmes: 75%"}, 
			{Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 14.0, "6 Filmes: 100%"}, 
			{Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6, filme7), 18.0, "7 Filmes: Sem desconto"}
		});
	}
	
	@Test
	public void deveCalcularValorLocacaoConsiderandoDescontos() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		
		//acao
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//verificao
		assertThat(resultado.getValor(), is(valorLocacao));
		
	}

}
