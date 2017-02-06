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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.Timer;

import componentes.ObjetoAnimado;

import jogo.SMPropulsion;

public class GasPropulsor implements ActionListener {
	private boolean ativo = false, viradoParaDireita = false;
	private int x, y, imgAtual;
	private SMPropulsion jogo;
	private ImageIcon imagens[];
	private Timer timer;
	
	public GasPropulsor(SMPropulsion jogo) {
		this.jogo = jogo;
		this.imagens = new ImageIcon[7];
		
		for(int i = 0; i < 4; ++i)
			this.imagens[i] = new ImageIcon("img/objetos/gas" + i + ".png");
		
		for(int i = 4; i < 7; ++i)
			this.imagens[i] = new ImageIcon("img/objetos/gas" + (6 - i) + ".png");

		this.timer = new Timer(20, this);
	}
	
	public void desenharGas(Graphics g) {
		if(this.viradoParaDireita)
			this.imagens[this.imgAtual].paintIcon(this.jogo, g, this.x - 20, this.y);
		else
			this.imagens[this.imgAtual].paintIcon(this.jogo, g, this.x + 10, this.y);
	}

	public void actionPerformed(ActionEvent e) {
		if(this.viradoParaDireita) this.x -= 3;
		else this.x += 3;
		
		this.y += jogo.getMario().vVertical / 10;
		
		if(this.imgAtual++ == 6) this.desativar();
	}
	
	public void ativar() {
		this.x = (int)this.jogo.getMario().getX();
		this.y = (int)this.jogo.getMario().getY() +
				 (this.jogo.getMario().getEstado() == ObjetoAnimado.ESTADO.COM_COGUMELO ? 10 : 2);
		
		this.viradoParaDireita = this.jogo.getMario().viradoParaDireita;
		this.ativo(true);
		this.imgAtual = 0;
		this.timer.start();
	}
	
	public void desativar() {
		this.jogo.getMario().soltandoGas(false);
		this.ativo(false);
		this.timer.stop();
	}
	
	public void ativo(boolean a) { this.ativo = a; }
	public boolean ativo() { return this.ativo; }
}