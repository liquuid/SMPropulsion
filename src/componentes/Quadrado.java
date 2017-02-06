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


import personagens.Mario;
import plataformas.Plataforma;


import jogo.SMPropulsion;

//uma plataforma com colisoes laterais
public class Quadrado extends Plataforma
{
	private static final long serialVersionUID = 1L;
	protected File som1_tock;

	public Quadrado(SMPropulsion jogo, Point p1, Point p2, String img)
	{
		super(jogo, p1, p2, img);
		this.som1_tock = new File("sound/wav/tock.wav");
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
}
