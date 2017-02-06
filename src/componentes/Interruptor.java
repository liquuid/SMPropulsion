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
import personagens.Mario;
import jogo.SMPropulsion;

public class Interruptor extends Cubo {
	public enum ESTADO {
		NAO_PISADO,
		PISADO;
	}
	
	private static final long serialVersionUID = 1L;
	private ESTADO estado;
	private final Point posOri;
	private final int oriSize;
	
	public Interruptor(SMPropulsion jogo, Point p1, String img) {
		super(jogo, p1, new Point(p1.x + 32, p1.y + 32), img, 2);
		this.posOri = new Point(p1);
		this.oriSize = 32;
		this.estado = ESTADO.NAO_PISADO;
	}
	
	public boolean testarChaoPara(ObjetoAnimado sprite)
	{
		if(pisadoPor(sprite))
		{
			//sprite.y = (sprite instanceof Mario ? getYRelativo() : this.y) - sprite.height;
			return true;
		}
		else return false;
	}
	
	public ESTADO estado() { return this.estado; }
	
	public void estado(ESTADO estado) {
		this.estado = estado;

		switch(estado) {
		case NAO_PISADO:
			this.y = this.posOri.y;
			this.height = this.oriSize;	
			break;
		case PISADO:
			this.y = this.posOri.y + this.oriSize / 2;
			this.height = this.oriSize / 2;
			break;
		}
	}
		
	public void testarColisoesCom(ObjetoAnimado sprite)  //deveria chamar metodo de Quadrado
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
			sprite.vEsquerda = 0;
			sprite.saltando = false;
			
			if(!(sprite instanceof Mario)) //se for um pnc
				sprite.moverParaDireita = true;
		}
		else if(colidiuEmBaixoCom(sprite))
		{
			sprite.vVertical = -sprite.vVertical;
		}
	}
	
	public void desenhar(Graphics g)
	{
		if(this.estado == ESTADO.NAO_PISADO)
			imagensCubo[0].paintIcon(jogo, g, this.getXRelativo(), this.getYRelativo());
		else
			imagensCubo[1].paintIcon(jogo, g, this.getXRelativo(), this.getYRelativo());
		
		if(SMPropulsion.DEBUG1_MOSTRAR_CONTORNO)
			this.jogo.desenharContorno(g, this.getXRelativo(), this.getYRelativo(), width, height);
	}
}
