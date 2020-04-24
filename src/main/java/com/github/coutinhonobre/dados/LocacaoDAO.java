package com.github.coutinhonobre.dados;

import java.util.List;

import com.github.coutinhonobre.entidades.Locacao;

public interface LocacaoDAO {

	public void salvar(Locacao locacao);

	public List<Locacao> obterLocacoesPendentes();
	
}
