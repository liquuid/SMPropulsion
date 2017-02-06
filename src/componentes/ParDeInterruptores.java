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

import java.awt.Graphics;
import java.awt.Point;
import java.io.File;

import personagens.Mario;

import jogo.SMPropulsion;

public class ParDeInterruptores extends Cubo {
	private static final long serialVersionUID = 1L;
	private Interruptor interruptor1, interruptor2;
	private static int espaco = 15;
	private File som1;
	
	public ParDeInterruptores(SMPropulsion jogo, Point p1) {
		super(jogo, p1, new Point(p1.x + 32*2 + espaco , p1.y + 32), null, 2);
		this.interruptor1 = new Interruptor(jogo, new Point(p1), "interruptorOn");
		this.interruptor2 = new Interruptor(jogo, new Point(p1.x + 32 + espaco, p1.y), "interruptorOff");
		this.interruptor2.estado(Interruptor.ESTADO.PISADO);
		this.som1 = new File("sound/wav/blip.wav");
	}
	
	public boolean testarChaoPara(ObjetoAnimado sprite)
	{
		if(this.interruptor1.testarChaoPara(sprite)) {
			if(this.interruptor1.estado() == Interruptor.ESTADO.NAO_PISADO) {
				this.interruptor1.estado(Interruptor.ESTADO.PISADO);
				this.interruptor2.estado(Interruptor.ESTADO.NAO_PISADO);
				
				sprite.y = (sprite instanceof Mario ? this.interruptor1.getYRelativo() : this.interruptor1.y) - sprite.height;
				
				this.jogo.reproduzirEfeitoSonoro(this.som1);
				this.jogo.pararMusica();
			}
			
			return true;
		}
		else if(this.interruptor2.testarChaoPara(sprite))
		{
			if(this.interruptor2.estado() == Interruptor.ESTADO.NAO_PISADO) {
				this.interruptor2.estado(Interruptor.ESTADO.PISADO);
				this.interruptor1.estado(Interruptor.ESTADO.NAO_PISADO);
				
				sprite.y = (sprite instanceof Mario ? this.interruptor2.getYRelativo() : this.interruptor2.y) - sprite.height;
				
				this.jogo.reproduzirEfeitoSonoro(this.som1);
				this.jogo.getFaseAtual().iniciarMusica();
			}
			
			return true;
		} else return false;
	}
		
	public void testarColisoesCom(ObjetoAnimado sprite)  //deveria chamar metodo de Quadrado
	{
		this.interruptor1.testarColisoesCom(sprite);
		this.interruptor2.testarColisoesCom(sprite);
	}
	
	public void desenhar(Graphics g) {
		this.interruptor1.desenhar(g);
		this.interruptor2.desenhar(g);
		
		if(SMPropulsion.DEBUG1_MOSTRAR_CONTORNO)
			this.jogo.desenharContorno(g, this.getXRelativo(), this.getYRelativo(), width, height);
	}
	
	public void resetar() {
		this.interruptor1.estado(Interruptor.ESTADO.NAO_PISADO);
		this.interruptor2.estado(Interruptor.ESTADO.PISADO);
	}
}
