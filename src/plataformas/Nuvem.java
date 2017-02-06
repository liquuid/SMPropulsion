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

package plataformas;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import componentes.ObjetoAnimado;
import etc.Animacao;
import etc.CorretorVertical;
import etc.Temporizador;
import personagens.Mario;


import jogo.SMPropulsion;

public class Nuvem extends Plataforma implements Runnable, Animacao
{
	public enum TIPO {
		MOVIMENTO_AUTOMATICO,
		DESLIZANTE,
		ESTATICA;
	}
	
	private static final long serialVersionUID = 1L;
	private int alcance, count, altF, velocidade = 2;
	private boolean pisado = false, movendo = false, moverParaDireita = false;
	private TIPO tipo;
	private Point posOri;
	
	public Nuvem(SMPropulsion jogo, Point p1, int alcance, String img, int alturaFin, TIPO tipo)
	{
		super(jogo, p1, new Point(p1.x + 48, p1.y - 17), "img/objetos/" + img);
		this.posOri = new Point(p1);
		this.tipo = tipo;
		this.alcance = alcance;
		this.altF = alturaFin;
	}
	
	public boolean testarChaoPara(ObjetoAnimado sprite)
	{
		if(sprite instanceof Mario) {
			if(pisadoPor(sprite))
			{
			
				this.personagemATratar = sprite;
				if(sprite.getMaxY() < this.altF - 20 || sprite.getMaxY() > this.altF + 20)
					new CorretorVertical(this.jogo, this, sprite, this.altF);
				
				boolean condicao = false;
				
				switch(this.tipo) {
				case MOVIMENTO_AUTOMATICO: condicao = (!this.pisado && !this.movendo && this.alcance > 0); break;
				case DESLIZANTE: condicao = (this.jogo.getMario().soltandoGas() && !this.movendo); break;
				case ESTATICA: condicao = false; break;
				}
				
				if(condicao) new Thread(this).start();
				
				this.pisado = true;
				return true;
			
			} else {
				this.pisado = false;
				return false;
			}
		} else return super.testarChaoPara(sprite);

	}
	
	public void desenhar(Graphics g)
	{
		imagem.paintIcon(jogo, g, this.getXRelativo() - 10, this.getYRelativo() - 15);
		
		if(SMPropulsion.DEBUG1_MOSTRAR_CONTORNO)
			this.jogo.desenharContorno(g, this.getXRelativo(), this.getYRelativo(), width, height);
	}
	
	public void run() {
		this.loop();
	}

	public void desenhar(Graphics2D gScr) {}

	public void loop() {
		Temporizador temp = new Temporizador(this, 15);
		
		this.count = 0;
		this.movendo = true;
		
		this.moverParaDireita = this.personagemATratar.viradoParaDireita;
		boolean condicao = true;
		
		while(condicao) {
			condicao = this.count < this.alcance;
			
			this.atualizar();
			temp.normalizar();
			temp.pularQuadros();
		}
		
		this.movendo = false;
	}
	
	public void atualizar() {
		++this.count;
		
		switch(this.tipo) {
		case MOVIMENTO_AUTOMATICO:
			if(count < this.alcance / 2) {
				if(this.pisado) this.personagemATratar.moverNaHorizontal(-20);
				this.x -= 2;
			} else {
				if(this.pisado) this.personagemATratar.moverNaHorizontal(20);
				this.x += 2;
			}
			break;
		case DESLIZANTE:
			velocidade = ((count < alcance - 15) ? 2 : 1);

			if(this.moverParaDireita){
				if(this.pisado) this.personagemATratar.moverNaHorizontal(velocidade*10);
				this.x += velocidade;
			}
			else {
				if(this.pisado) this.personagemATratar.moverNaHorizontal(-velocidade*10);
				this.x -= velocidade;
			}
			break;
		}
	}
	
	public void resetar() {
		this.x = this.posOri.x;
		this.y = this.posOri.y;
	}
}
