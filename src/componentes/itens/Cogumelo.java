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

package componentes.itens;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.Timer;

import personagens.Mario;
import componentes.Cubo;
import componentes.ObjetoAnimado;
import jogo.SMPropulsion;

public class Cogumelo extends ObjetoAnimado implements ActionListener
{
	private static final long serialVersionUID = 1L;
	public boolean ativo = false;
	public boolean mostrarImagem = false, inicializado = false;
	protected ImageIcon imagem;
	private File som;
	private final Point posIni;
	private Timer timer3;
	private int piscou = 0;
	
	public Cogumelo(Cubo cubo, SMPropulsion jogo, Point p)
	{
		super(jogo, new Point(p), new Point(p.x + 16, p.y - 16));
		
		this.posIni = new Point(p);
		
		imagem = new ImageIcon("img/objetos/cogumelo.png");
		this.som = new File("sound/wav/powerup.wav");
		this.timer1 = new Timer(150, this);
		this.timer2 = new Timer(5000, this);
		this.timer3 = new Timer(200, this);
	}
	
	public void piscar()
	{
		mostrarImagem = !mostrarImagem;
	}
	
	public void moverParaDireita()
	{
		++this.x;
	}
	
	public void moverParaEsquerda()
	{
		--this.x;
	}
	
	public void moverParaCima()
	{
		--this.y;
	}
	
	public void moverParaBaixo()
	{
		++this.y;
	}
	
	public void testarColisoesCom(ObjetoAnimado sprite)
	{
		if(colisaoAEsquerdaCom(sprite))
		{
			if(sprite instanceof Mario) {
				this.jogo.reproduzirEfeitoSonoro(this.som);
				jogo.getMario().crescer();
				this.desativar();
			} else {
				//colisao com outros PNCs
				if(!(sprite instanceof Mario)) //se for um npc
				{
					sprite.moverParaDireita = false;
					this.moverParaDireita = true;
				}
			}
		}
		else if(colisaoADireitaCom(sprite))
		{
			if(sprite instanceof Mario) {
				this.jogo.reproduzirEfeitoSonoro(this.som);
				jogo.getMario().crescer();
				this.desativar();
			} else {
				//colisao com outros PNCs
				if(!(sprite instanceof Mario)) //se for um npc
				{
					sprite.moverParaDireita = true;
					this.moverParaDireita = false;
				}
			}
		}
	}
	
	public void desenhar(Graphics g)
	{
		if(mostrarImagem)
			imagem.paintIcon(jogo, g, this.getXRelativo(), this.getYRelativo());
		
		if(SMPropulsion.DEBUG1_MOSTRAR_CONTORNO)
			this.jogo.desenharContorno(g, this.getXRelativo(), this.getYRelativo(), width, height);
	}
	
	public void resetar() {
		this.x = this.posIni.x;
		this.y = this.posIni.y;
		this.ativo = false;
		this.piscou = 0;
		this.inicializado = false;
		this.moverParaDireita = true;
		this.timer1.stop();
	}
	
	public void atualizar()
	{
		if(!jogo.estaPausado() && this.inicializado)
		{
			sofrerGravidade();
			moverNaVertical();
			//jogo.getFaseAtual().testarColisaoPara(this);
			
			if(moverParaDireita) moverParaDireita();
			else moverParaEsquerda();
		}
		else if(!this.inicializado) {
			--this.y;
		}
	}
	
	public void moverNaHorizontal(int vel) {
		// TODO Auto-generated method stub
		
	}

	public void desativar() {
		this.inicializado = false;
		this.ativo = false;
		this.timer1.stop();
		this.timer2.stop();
		this.timer3.stop();
	}

	public void ativar() {
		this.mostrarImagem = true;
		this.ativo = true;
		this.timer1.start();
		this.timer2.start();
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.timer1) {
			this.inicializado = true;
			this.timer1.stop();
		}
		
		if(e.getSource() == this.timer2) {
			this.timer2.stop();
			this.timer3.start();
		}
		
		if(e.getSource() == this.timer3)
			if(this.piscou++ > 5) this.desativar();
			else this.mostrarImagem = !this.mostrarImagem;
	}
}
