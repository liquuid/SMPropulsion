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

import javax.swing.ImageIcon;

import personagens.Mario;
import etc.CorretorVertical;

import jogo.SMPropulsion;

public class Blocos extends Quadrado {
	
	public enum TIPO {
		HORIZONTAL,
		VERTICAL;
	}
	
	private static final long serialVersionUID = 1L;
	private int alturaDaTela;
	private boolean colidiuEmBaixo = false;
	
	public Blocos(SMPropulsion jogo, Point p1, int altTela, TIPO tipo) {
		super(jogo, p1, p1, null);
		this.x = p1.x;
		this.y = p1.y;
		
		switch(tipo) {
		case HORIZONTAL:
			this.width = 189;
			this.height = 48;
			this.imagem = new ImageIcon("img/objetos/blocosh.png"); break;
		case VERTICAL:
			this.width = 48;
			this.height = 189;
			this.imagem = new ImageIcon("img/objetos/blocosv.png"); break;
		}
		
		this.alturaDaTela = altTela;
	}
	
	public boolean testarChaoPara(ObjetoAnimado sprite)
	{
		if(sprite instanceof Mario) {
			if(pisadoPor(sprite))
			{
				this.personagemATratar = sprite;
				if(sprite.getMaxY() < this.alturaDaTela - 20 || sprite.getMaxY() > this.alturaDaTela + 20)
					new CorretorVertical(this.jogo, this, sprite, this.alturaDaTela);
				
				sprite.y = (sprite instanceof Mario ? getYRelativo() : this.y) - sprite.height;
				return true;
			}
			else
				return false;

		} else return super.testarChaoPara(sprite);
	}
	
	public void testarColisoesCom(ObjetoAnimado sprite)
	{
		if(colisaoAEsquerdaCom(sprite))
		{
			sprite.vDireita = 0;
			sprite.saltando = false;
			
			if(!(sprite instanceof Mario)) //se for um pnc
				sprite.moverParaDireita = false;

		}
		else if(colisaoADireitaCom(sprite))
		{

			sprite.vDireita = 10;
			sprite.vEsquerda = 0;
			
			sprite.saltando = false;
			
			if(!(sprite instanceof Mario)) //se for um pnc
				sprite.moverParaDireita = true;
		}
		else if(colidiuEmBaixoCom(sprite))
		{
			sprite.vVertical = 0;
			
			if(!this.colidiuEmBaixo) {
				this.jogo.reproduzirEfeitoSonoro(this.som1_tock);
				this.colidiuEmBaixo = true;
			}
		}
		else this.colidiuEmBaixo = false;
	}
}
