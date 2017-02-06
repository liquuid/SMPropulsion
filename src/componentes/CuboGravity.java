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
import jogo.SMPropulsion;

public class CuboGravity extends Cubo {
	private static final long serialVersionUID = 1L;
	public static int imagem = 0;
	private File som;
	
	public CuboGravity(SMPropulsion jogo, Point p1, String img, int nImgs)
	{
		super(jogo, p1, new Point(p1.x + 47, p1.y - 16), img, 2);
		this.som = new File("sound/wav/stomp.wav");
	}
	
	public void testarColisoesCom(ObjetoAnimado sprite)
	{
		if(colisaoAEsquerdaCom(sprite)) {
			sprite.vDireita = 0;
			sprite.saltando = false;
		} else if(colisaoADireitaCom(sprite)) {
			sprite.vEsquerda = 0;
			sprite.saltando = false;
		} else if(colidiuEmBaixoCom(sprite)) {
			sprite.vVertical = -sprite.vVertical;
			this.jogo.getFaseAtual().gravidade(!this.jogo.getFaseAtual().gravidade());

			this.jogo.reproduzirEfeitoSonoro(this.som);
			CuboGravity.imagem = (CuboGravity.imagem + 1) % 2;
		}
	}
	
	public void desenhar(Graphics g)
	{
		imagensCubo[CuboGravity.imagem].paintIcon(jogo, g, this.getXRelativo(), this.getYRelativo());
	
		if(SMPropulsion.DEBUG1_MOSTRAR_CONTORNO)
			this.jogo.desenharContorno(g, this.getXRelativo(), this.getYRelativo(), width, height);
	}
	
	public void resetar() {

	}
}