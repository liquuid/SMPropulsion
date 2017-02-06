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

package etc;

import java.awt.Graphics;
import java.awt.Point;
import javax.swing.ImageIcon;
import jogo.SMPropulsion;

public class Relogio {
	private Point posicao;
	private SMPropulsion jogo;
	private ImageIcon imagem;
	
	public Relogio(SMPropulsion jogo) {
		this.jogo = jogo;
		this.posicao = new Point(452, 75);
		this.imagem = new ImageIcon("img/visor/relogio.png");
	}

	public void desenhar(Graphics g) {
		this.imagem.paintIcon(this.jogo, g, this.posicao.x, this.posicao.y);
		this.jogo.digito(this.getCentena()).paintIcon(this.jogo, g, this.posicao.x + 15, this.posicao.y);
		this.jogo.digito(this.getDezena()).paintIcon(this.jogo, g, this.posicao.x + 25, this.posicao.y);
		this.jogo.digito(this.getUnidade()).paintIcon(this.jogo, g, this.posicao.x + 35, this.posicao.y);
	}
	
	private int getUnidade() {
		return (this.jogo.getTempoDecorrido() % 10);
	}
	
	private int getDezena() {
		return (this.jogo.getTempoDecorrido() / 10) % 10;
	}
	
	private int getCentena() {
		return (this.jogo.getTempoDecorrido() / 100);
	}
}
