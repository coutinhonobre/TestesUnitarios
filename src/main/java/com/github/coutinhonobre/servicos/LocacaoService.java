package com.github.coutinhonobre.servicos;

import static com.github.coutinhonobre.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.github.coutinhonobre.dados.LocacaoDAO;
import com.github.coutinhonobre.entidades.Filme;
import com.github.coutinhonobre.entidades.Locacao;
import com.github.coutinhonobre.entidades.Usuario;
import com.github.coutinhonobre.exceptions.FilmeSemEstoqueException;
import com.github.coutinhonobre.exceptions.LocadoraException;
import com.github.coutinhonobre.utils.DataUtils;

public class LocacaoService {
	
	private LocacaoDAO dao;
	private SPCService spcService;
	private EmailService emailService;

	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException {

		Double valorLocacao = 0.0;

		if (usuario == null) {
			throw new LocadoraException("Usuario vazio");
		}

		if (filmes == null || filmes.isEmpty()) {
			throw new LocadoraException("Filme vazio");
		}

		for (int i = 0; i < filmes.size(); i++) {
			if (filmes.get(i).getEstoque() == 0) {
				throw new FilmeSemEstoqueException();
			}

			switch (i) {
				case 2:
					valorLocacao += filmes.get(i).getPrecoLocacao() * 0.75;
					break;
				case 3:
					valorLocacao += filmes.get(i).getPrecoLocacao() * 0.50;
					break;
				case 4:
					valorLocacao += filmes.get(i).getPrecoLocacao() * 0.25;
					break;
				case 5:
					valorLocacao += 0.0;
					break;
				default:
					valorLocacao += filmes.get(i).getPrecoLocacao();
					break;
			}

		}
		
		if(spcService.possuiNegativacao(usuario)) {
			throw new LocadoraException("Usuário Negativado");
		}

		Locacao locacao = new Locacao();
		locacao.setFilme(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		locacao.setValor(valorLocacao);

		// Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) dataEntrega = adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);

		// Salvando a locacao...
		dao.salvar(locacao);

		return locacao;
	}
	
	public void notificarAtrasos() {
		List<Locacao> locacoes = dao.obterLocacoesPendentes();
		for(Locacao locacao: locacoes) {
			emailService.notificarAtraso(locacao.getUsuario());
		}
	}
	
	public void setLocacaoDAO(LocacaoDAO dao) {
		this.dao = dao;
	}
	
	public void setSPCService(SPCService spc) {
		spcService = spc;
	}
	
	
	public void setEmailService(EmailService email) {
		emailService = email;
	}
	
	

}