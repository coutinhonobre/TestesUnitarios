package com.github.coutinhonobre.servicos;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.coutinhonobre.builders.FilmeBuilder;
import com.github.coutinhonobre.builders.LocacaoBuilder;
import com.github.coutinhonobre.builders.UsuarioBuilder;
import com.github.coutinhonobre.dados.LocacaoDAO;
import com.github.coutinhonobre.entidades.Filme;
import com.github.coutinhonobre.entidades.Locacao;
import com.github.coutinhonobre.entidades.Usuario;
import com.github.coutinhonobre.exceptions.FilmeSemEstoqueException;
import com.github.coutinhonobre.exceptions.LocadoraException;
import com.github.coutinhonobre.matchers.MatchersProprios;
import com.github.coutinhonobre.utils.DataUtils;

public class LocacaoServiceTest {

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	private LocacaoService service;
	
	private LocacaoDAO dao;
	
	private SPCService spc;
	
	private EmailService email;
	
	
	@Before
	public void setup() {
		service = new LocacaoService();
		dao = Mockito.mock(LocacaoDAO.class);
		service.setLocacaoDAO(dao);
		spc = Mockito.mock(SPCService.class);
		service.setSPCService(spc);
		email = Mockito.mock(EmailService.class);
		service.setEmailService(email);
	}

	@Test
	public void deveAlugarFilme() throws Exception {
		
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		// cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		Filme filme = FilmeBuilder.umFilme().comValor(5.0).agora();
		List<Filme> listFilme = new ArrayList<Filme>();
		listFilme.add(filme);
		

		// acao
		Locacao locacao = service.alugarFilme(usuario, listFilme);

		// verificacao
		// assertThat verifique que
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(locacao.getValor(), is(not(6.0)));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)),
				is(true));
		error.checkThat(locacao.getDataRetorno(), MatchersProprios.ehHojeComDiferencaDias(1));
		error.checkThat(locacao.getDataLocacao(), MatchersProprios.ehHoje());

	}

	@Test(expected = FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmeSemEstoque() throws Exception {
		// cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		Filme filme = FilmeBuilder.umFilmeSemEstoque().agora();
		List<Filme> listFilme = new ArrayList<Filme>();
		listFilme.add(filme);

		// acao
		service.alugarFilme(usuario, listFilme);
	}

	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
		// cenario
		Filme filme = FilmeBuilder.umFilme().agora();
		List<Filme> listFilme = new ArrayList<Filme>();
		listFilme.add(filme);

		// acao
		try {
			service.alugarFilme(null, listFilme);
			fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario vazio"));
		}
	}

	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		
		// acao
		service.alugarFilme(usuario, null);
		
	}
	
	@Test
	public void devePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> listFilme = Arrays.asList(
				FilmeBuilder.umFilme().agora(),
				FilmeBuilder.umFilme().agora(),
				FilmeBuilder.umFilme().agora()
				);
		
		//acao
		Locacao resultado = service.alugarFilme(usuario, listFilme);
		
		//verificao
		assertThat(resultado.getValor(), is(11.0));
	}
	
	@Test
	public void devePagar50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> listFilme = Arrays.asList(
				FilmeBuilder.umFilme().agora(),
				FilmeBuilder.umFilme().agora(),
				FilmeBuilder.umFilme().agora(),
				new Filme("Filme 4", 2, 4.0)
				);
		
		//acao
		Locacao resultado = service.alugarFilme(usuario, listFilme);
		
		//verificao
		assertThat(resultado.getValor(), is(13.0));
	}
	
	@Test
	public void devePagar25PctNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> listFilme = Arrays.asList(
				FilmeBuilder.umFilme().agora(),
				FilmeBuilder.umFilme().agora(),
				FilmeBuilder.umFilme().agora(),
				new Filme("Filme 4", 2, 4.0),
				new Filme("Filme 5", 2, 4.0)
				);
		
		//acao
		Locacao resultado = service.alugarFilme(usuario, listFilme);
		
		//verificao
		assertThat(resultado.getValor(), is(14.0));
	}
	
	@Test
	public void devePagar0PctNoFilme6() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> listFilme = Arrays.asList(
				FilmeBuilder.umFilme().agora(),
				FilmeBuilder.umFilme().agora(),
				FilmeBuilder.umFilme().agora(),
				FilmeBuilder.umFilme().agora(),
				FilmeBuilder.umFilme().agora(),
				FilmeBuilder.umFilme().agora()
				);
		
		//acao
		Locacao resultado = service.alugarFilme(usuario, listFilme);
		
		//verificao
		assertThat(resultado.getValor(), is(14.0));
	}
	
	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> listFilme = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		//acao
		Locacao retorno = service.alugarFilme(usuario, listFilme);
		
		
		//verificao
		boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
		assertTrue(ehSegunda);
		//assertThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
		assertThat(retorno.getDataRetorno(), MatchersProprios.caiEm(Calendar.MONDAY));
		assertThat(retorno.getDataRetorno(), MatchersProprios.caiNumaSegunda());
	}
	
	
	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws FilmeSemEstoqueException {
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuario 2").agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		when(spc.possuiNegativacao(usuario)).thenReturn(true);
		
		//acao
		try {
			service.alugarFilme(usuario, filmes);
		//verificacao
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Usuário Negativado"));
		}
		
		verify(spc).possuiNegativacao(usuario);
	}
	
	
	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {
		// cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuario em dia").agora();
		List<Locacao> locacoes = Arrays.asList(
				LocacaoBuilder.umLocacao()
					.comUsuario(usuario)
					.comDataRetorno(DataUtils.obterDataComDiferencaDias(-2))
					.agora(),
					LocacaoBuilder.umLocacao().comUsuario(usuario2).agora());
		// gravando a expectativa
		when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
		
		// acao
		service.notificarAtrasos();
		
		// verificacao
		verify(email).notificarAtraso(usuario);
	}

}
