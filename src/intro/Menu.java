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

package intro;

import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import jogo.SMPropulsion;

public abstract class Menu  extends KeyAdapter {
	protected MenuInicial menuInicial;
	protected SMPropulsion jogo;
	protected ImageIcon fundo;
	protected boolean mostrar = false;
	
	public Menu(MenuInicial menuInicial, SMPropulsion jogo, String img) {
		this.jogo = jogo;
		this.menuInicial = menuInicial;
		this.fundo = new ImageIcon(img);
	}
	
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.jogo.reproduzirEfeitoSonoro(this.menuInicial.som2);
			this.mostrar = false;
			this.menuInicial.jogo.removeKeyListener(this);
			this.menuInicial.inicializar();
		}
	}
	
	public void desenhar(Graphics2D gScr) {
		this.fundo.paintIcon(this.jogo, gScr, 100, 230);
	}
}
