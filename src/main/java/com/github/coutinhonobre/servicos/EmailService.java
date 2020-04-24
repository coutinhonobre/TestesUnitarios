package com.github.coutinhonobre.servicos;

import com.github.coutinhonobre.entidades.Usuario;

public interface EmailService {
	
	public void notificarAtraso(Usuario usuario);

}
