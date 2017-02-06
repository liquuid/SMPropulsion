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

import personagens.Mario;

import jogo.SMPropulsion;

public class InterruptorFinalizador extends Interruptor {
	private static final long serialVersionUID = 1L;
	
	public InterruptorFinalizador(SMPropulsion jogo, Point p1, String img) {
		super(jogo, p1, img);
	}
	
	public boolean testarChaoPara(ObjetoAnimado sprite)
	{
		if(pisadoPor(sprite) && this.estado() != ESTADO.PISADO)
		{
			this.estado(ESTADO.PISADO);
			sprite.y = (sprite instanceof Mario ? getYRelativo() : this.y) - sprite.height;
			
			this.jogo.terminar();
			return true;
		}
		else return false;
	}
	
	public void resetar() {
		this.estado(ESTADO.NAO_PISADO);
	}
}
