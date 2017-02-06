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
import java.awt.Point;
import javax.swing.ImageIcon;
import componentes.ObjetoTangivel;
import componentes.ObjetoAnimado;
import jogo.SMPropulsion;

public class Plataforma extends ObjetoTangivel
{	
	private static final long serialVersionUID = 1L;
	protected ImageIcon imagem;
	
	public Plataforma(SMPropulsion jogo, Point p1, Point p2, String img)
	{
		super(jogo, p1, p2);

		if(img != null)
			imagem = new ImageIcon(img);
	}
	
	public void testarColisoesCom(ObjetoAnimado sprite)
	{
		if(colisaoAEsquerdaCom(sprite) || colisaoADireitaCom(sprite))
			jogo.getMario().inverterDirecao();
	}
	
	public void desenhar(Graphics g)
	{
		if(imagem != null)
			imagem.paintIcon(jogo, g, this.getXRelativo(), this.getYRelativo());
		
		if(SMPropulsion.DEBUG1_MOSTRAR_CONTORNO)
			this.jogo.desenharContorno(g, this.getXRelativo(), this.getYRelativo(), width, height);
	}

	public void resetar() {
		//TODO ?
	}
}
