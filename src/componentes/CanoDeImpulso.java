/*   
 *   Super Mario Propulsion
 *   Copyright (C) Este programa é software livre; você pode redistribuí-lo e/ou
 *   modificá-lo sob os termos da Licença Pública Geral GNU, conforme
 *   publicada pela Free Software Foundation; tanto a versão 2 da
 *   Licença como (a seu critério) qualquer versão mais nova.
 *
 *   Este programa é distribuído na expectativa de ser útil, mas SEM
 *   QUALQUER GARANTIA; sem mesmo a garantia implícita de
 *   COMERCIALIZAÇÃO ou de ADEQUAÇÃO A QUALQUER PROPÓSITO EM
 *   PARTICULAR. Consulte a Licença Pública Geral GNU para obter mais
 *   detalhes.
 *
 *   Você deve ter recebido uma cópia da Licença Pública Geral GNU
 *   junto com este programa; se não, escreva para a Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 *   02111-1307, USA.
 */

package componentes;

import java.awt.Point;
import java.io.File;

import etc.Temporizador;

import personagens.Mario;


import jogo.SMPropulsion;

public class CanoDeImpulso extends Quadrado implements Runnable
{
	public enum Tipo
	{
		LANCADOR_HORIZONTAL,
		LANCADOR_VERTICAL;
	}
	
	private static final long serialVersionUID = 1L;
	private int alcance;
	private Tipo tipo;
	private boolean ativo = false;
	private File som1_entrando;
	private File som2_lancando;
	
	public CanoDeImpulso(SMPropulsion jogo, Point p1, Point p2, Tipo tipo, int alcance, String img)
	{
		super(jogo, p1, p2, img);
		
		this.alcance = alcance;
		this.tipo = tipo;
		this.som1_entrando = new File("sound/wav/down.wav");
		this.som2_lancando = new File("sound/wav/bumm.wav");
	}
	
	public boolean testarChaoPara(ObjetoAnimado sprite)
	{
		if(pisadoPor(sprite))
		{
			if(jogo.getMario().baixo &&
			   sprite.x + sprite.width > this.getXRelativo() + 17 &&
			   sprite.x < this.getXMaxRelativo() - 17)
			{
				ativo = true;
				personagemATratar = sprite;
				(new Thread(this)).start();
				return true;
			}
			else if(jogo.getMario().controlavel)
				sprite.y = this.getYRelativo() - sprite.height;
			
			return true;
		}
		else return ativo;
	}
	
	private void moverPersonagemParaDentro()
	{
		personagemATratar.controlavel = false;
		personagemATratar.baixo = false;
		personagemATratar.vDireita = 0;
		personagemATratar.vEsquerda = 0;
		personagemATratar.levantar();
		
		if(personagemATratar instanceof Mario)
			switch(personagemATratar.getEstado())
			{
				case SEM_ITEM: personagemATratar.imagemAtual = 6; break;
				case COM_COGUMELO: personagemATratar.imagemAtual = 7; break;
			}
			
		Temporizador temp = new Temporizador(this.jogo, 40);
		this.jogo.reproduzirEfeitoSonoro(this.som1_entrando);
		for(int i = 0; i < 40; ++i)
		{			
			if(personagemATratar.getMaxY() < this.getYMaxRelativo())
				++personagemATratar.y;
			
			temp.normalizar();
		}
		
		try{Thread.sleep(200);}
		catch(Exception e){e.printStackTrace();}
		
		this.jogo.reproduzirEfeitoSonoro(this.som2_lancando);
		
		if(tipo == Tipo.LANCADOR_HORIZONTAL)
		{
			personagemATratar.vVertical = 20;
			personagemATratar.vDireita = alcance;
		}
		else if(tipo == Tipo.LANCADOR_VERTICAL)
		{
			personagemATratar.vVertical = alcance;
		}

		personagemATratar.controlavel = true;
		ativo = false;
	}
	
	public void run()
	{ //apenas mario pode entrar no cano?
		moverPersonagemParaDentro();
	}
}
