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

import javax.swing.ImageIcon;

import jogo.SMPropulsion;

public class CuboSpeed extends Cubo
{
	public enum Tipo {
		DIREITA,
		ESQUERDA,
		CIMA,
		BAIXO;
	}
	
	private static final long serialVersionUID = 1L;
	private File som;
	private int velocidade;
	private Tipo tipo;
	
	public CuboSpeed(SMPropulsion jogo, Point p1, Tipo tipo, int velocidade)
	{
		super(jogo, p1, new Point(p1.x + 16, p1.y - 16), "", 1);
		
		switch(tipo) {
			case DIREITA: imagensCubo[0] = new ImageIcon("img/objetos/speedd0.png"); break;
			case ESQUERDA: imagensCubo[0] = new ImageIcon("img/objetos/speede0.png"); break;
			case CIMA: imagensCubo[0] = new ImageIcon("img/objetos/speedc0.png"); break;
			case BAIXO: imagensCubo[0] = new ImageIcon("img/objetos/speedb0.png"); break;
		}
		
		this.tipo = tipo;
		this.velocidade = velocidade;
		this.som = new File("sound/wav/wahoo.wav");
	}
	
	public void testarColisoesCom(ObjetoAnimado sprite)
	{
		if(this.colidiu(sprite)) {
			if(!this.jogo.estaReproduzindoSom())
				this.jogo.reproduzirEfeitoSonoro(this.som);
			
			switch(this.tipo) {
			case CIMA: case BAIXO:
				sprite.vDireita = 0;
				sprite.vEsquerda = 0;
				sprite.vVertical = this.velocidade;
				break;
			case ESQUERDA:
				sprite.vVertical = 15;
				sprite.vDireita = 0;
				sprite.vEsquerda = this.velocidade;
				break;
			case DIREITA:
				sprite.vVertical = 15;
				sprite.vEsquerda = 0;
				sprite.vDireita = this.velocidade;
				break;
			}
		}
	}
	
	public boolean testarChaoPara(ObjetoAnimado sprite)
	{
		return false;
	}
	
	public void resetar() {

	}
}
