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

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.Timer;

import jogo.SMPropulsion;

public class MarioAnimacao implements ActionListener, Animacao
{
	private SMPropulsion jogo;
	private final String IMAGE_DIR = "img/mario/";
	private ImageIcon imagens[];
	private int imgAtual;
	private int x, y, count;
	private Timer timer;
	private boolean ativo;
	private int numQuadros;
	
	public MarioAnimacao()
	{
		this.imagens = new ImageIcon[4];
		
		for(int i = 0; i < 4; ++i)
			this.imagens[i] = new ImageIcon(this.IMAGE_DIR + "mario" + i + "d.png");
		
		this.imgAtual = 0;
		
		this.x = -30;
		this.y = 270;
		this.timer = new Timer(90, this);
		this.numQuadros = 2;
		
		this.ativo = true;
		this.iniciarAnimacao();
	}
	
	public MarioAnimacao(int x, int y, int time)
	{
		this.imagens = new ImageIcon[13];
				
		this.imagens[0] = new ImageIcon(this.IMAGE_DIR + "marioIntro" + 1 + "b.gif");
		this.imagens[1] = new ImageIcon(this.IMAGE_DIR + "marioIntro" + 2 + "b.gif");
		this.imagens[2] = new ImageIcon(this.IMAGE_DIR + "marioIntro" + 3 + "b.gif");
		this.imagens[3] = this.imagens[0];
		this.imagens[4] = this.imagens[1];
		this.imagens[5] = this.imagens[2];
		this.imagens[6] = new ImageIcon(this.IMAGE_DIR + "marioIntro" + 1 + ".gif");
		this.imagens[7] = new ImageIcon(this.IMAGE_DIR + "marioIntro" + 2 + ".gif");
		this.imagens[8] = new ImageIcon(this.IMAGE_DIR + "marioIntro" + 3 + ".gif");
		this.imagens[9] = this.imagens[6];
		this.imagens[10] = this.imagens[7];
		this.imagens[11] = this.imagens[8];
		this.imagens[12] = new ImageIcon(this.IMAGE_DIR + "mario3e.png");
		
		this.imgAtual = 0;
		
		this.x = x;
		this.y = y;
		this.timer = new Timer(time, this);
		
		this.numQuadros = 12;
		this.ativo = true;
	}
	
	public MarioAnimacao(int x, int y, int nImgs, String img, int time, SMPropulsion jogo) //d
	{
		this.jogo = jogo;
		this.imagens = new ImageIcon[nImgs];
		
		for(int i = 0; i < nImgs; ++i)
			this.imagens[i] = new ImageIcon(this.IMAGE_DIR + img + i + ".gif");
		
		this.imgAtual = 0;
		
		this.x = x;
		this.y = y;
		this.timer = new Timer(time, this);
		
		this.numQuadros = nImgs;
		this.ativo = true;
		this.count = 0;
	}
			
	public void iniciarAnimacao(){
		if(!this.timer.isRunning()){
			this.imgAtual = 0;
			this.timer.start();
		}
	}
	
	public void pararAnimacao() { if(this.timer.isRunning()) this.timer.stop(); }
	
	public int imagemAtual() { return this.imgAtual; }
	public void imagemAtual(int img) { this.imgAtual = img; }
	
	public int x() { return this.x; }
	public void x(int x) { this.x = x; }
	public int y() { return this.y; }
	public void y(int y) { this.y = y; }
	
	public void desenhar(Graphics2D g2d)
	{
		g2d.drawImage(this.imagens[imgAtual].getImage(), this.x, this.y, null);
	}
	
	public boolean ativo() { return this.ativo; }
	public void ativo(boolean a) { this.ativo = a; }

	public void actionPerformed(ActionEvent e)
	{
		this.imgAtual = (this.imgAtual + 1) % this.numQuadros;
	}

	public void atualizar() {
		++this.count;
		
		if(this.count == 70) this.iniciarAnimacao();
		
		if(this.count > 100)
			this.y(this.y() + 1);

		if(this.y() > this.jogo.getAlturaDaTela() && !this.jogo.estaReproduzindoSom()) {
			this.pararAnimacao();
			
			if(this.jogo.getMario().vidas() > 0) {
				this.jogo.reiniciarTempo();
				this.jogo.getFaseAtual().reiniciar();
			}
			else{
				if(this.count == 300) this.jogo.getFaseAtual().gameOver(true);
				else if(this.count > 500) this.jogo.getFaseAtual().ativa(false);
			}
		}
	}
	
	public void count(int c) { this.count = c; }

	public void loop() {

	}
}