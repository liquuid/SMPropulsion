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

package personagens.inimigos;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import componentes.ObjetoAnimado;
import personagens.Mario;
import jogo.SMPropulsion;

public class Crocodilo extends Inimigo
{
	private static final long serialVersionUID = 1L;
	
	public int estado = 0;
	private int imagemAtual = 0;
	private ImageIcon[] imagensCrocodiloEsq;
	private ImageIcon[] imagensCrocodiloDir;
	private Timer timer1;
	private Timer timer2;
	private final Point oriPos;
	private int vel = 1;
	
	public Crocodilo(int cobertura, SMPropulsion jogo, Point p1)
	{
		super(cobertura, jogo, p1, new Point(p1.x + 20, p1.y - 32));
		this.oriPos = new Point(p1.x, p1.y);
		
		imagensCrocodiloDir = new ImageIcon[5];
		imagensCrocodiloEsq = new ImageIcon[5];
		
		imagensCrocodiloDir[0] = new ImageIcon("img/inimigos/crocodilo0d.png");
		imagensCrocodiloDir[1] = new ImageIcon("img/inimigos/crocodilo1d.png");
		imagensCrocodiloDir[2] = new ImageIcon("img/inimigos/crocodilof0d.png");
		imagensCrocodiloDir[3] = new ImageIcon("img/inimigos/crocodilof1d.png");
		imagensCrocodiloDir[4] = new ImageIcon("img/inimigos/crocodilofFd.png");
		
		imagensCrocodiloEsq[0] = new ImageIcon("img/inimigos/crocodilo0e.png");
		imagensCrocodiloEsq[1] = new ImageIcon("img/inimigos/crocodilo1e.png");
		imagensCrocodiloEsq[2] = new ImageIcon("img/inimigos/crocodilof0e.png");
		imagensCrocodiloEsq[3] = new ImageIcon("img/inimigos/crocodilof1e.png");
		imagensCrocodiloEsq[4] = new ImageIcon("img/inimigos/crocodilofFe.png");
		
		timer1 = new Timer(150, new Animador());
		timer2 = new Timer(500, new Finalizador());
		
		ativar();
	}

	public void desenhar(Graphics g)
	{	
		if(moverParaDireita)
			imagensCrocodiloDir[imagemAtual].paintIcon(jogo, g, this.getXRelativo(), this.getYRelativo());
		else
			imagensCrocodiloEsq[imagemAtual].paintIcon(jogo, g, this.getXRelativo(), this.getYRelativo());
	
		if(SMPropulsion.DEBUG1_MOSTRAR_CONTORNO)
			this.jogo.desenharContorno(g, this.getXRelativo(), this.getYRelativo(), width, height);
	}
	
	public void testarColisoesCom(ObjetoAnimado sprite)
	{
		if(ativo && imagemAtual != 4)
			if(colisaoAEsquerdaCom(sprite))
			{
				if(sprite instanceof Mario) {
					if(jogo.getMario().getEstado() == ESTADO.SEM_ITEM && !jogo.getMario().imortal)
					{				
						jogo.getMario().morrer();
					}
					else
					{
						jogo.getMario().piscar();
					}
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
				if(sprite instanceof Mario)
					if(jogo.getMario().getEstado() == ESTADO.SEM_ITEM &&
					   !jogo.getMario().imortal)
					{
						jogo.getMario().morrer();
					}
					else
					{
						jogo.getMario().piscar();
					}
				else
				{
					//colisao com outros PNCs
					if(!(sprite instanceof Mario)) //se for um npc
					{
						sprite.moverParaDireita = true;
						this.moverParaDireita = false;
					}
				}
			}
			else if(pisadoPor(sprite) && sprite.noAr)
			{
				switch(estado)
				{
				case 0:
					sprite.kickar();
					timer1.setDelay(70);
					imagemAtual = 2;
					vel = 2;
					this.y += 16;
					this.setSize(width, height - 16);
					break;
				case 2:
					timer1.stop();
					sprite.kickar();
					imagemAtual = 4;
					timer2.start();
					break;
				}

				estado += 2;
			}
	}
	
	public void moverParaDireita()
	{
		this.x += vel;
	}
	
	public void moverParaEsquerda()
	{
		this.x -= vel;
	}
	
	public void ativar()
	{
		ativo = true;
		if(!timer1.isRunning()) timer1.start();
	}
	
	public void desativar()
	{
		ativo = false;
		if(timer2.isRunning()) timer2.stop();
		if(timer1.isRunning()) timer1.stop();
	}
		
	public void atualizar()
	{
		if(!jogo.estaPausado() && imagemAtual != 4)
		{
			sofrerGravidade();
			moverNaVertical();
			jogo.getFaseAtual().testarColisaoPara(this);
			
			if(moverParaDireita) moverParaDireita();
			else moverParaEsquerda();
		
			if(this.x >= xIncial + cobertura) moverParaDireita = false;
			if(this.x <= xIncial - cobertura) moverParaDireita = true;
		}
	}

	private class Animador implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(ativo && timer1.isRunning() && imagemAtual != 4)
				imagemAtual = estado + ((imagemAtual + 1) % 2);
		}
	}
	
	private class Finalizador implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			Crocodilo.this.desativar();
			Crocodilo.this.encerrado = true;
		}
	}

	@Override
	public void moverNaHorizontal(int vel) {
		// TODO Auto-generated method stub
		
	}
	
	public void testarAtivacao()
	{
	//	boolean estaPerto = Math.abs(this.jogo.getMario().getY() - this.getYRelativo()) < this.jogo.getAlturaDaTela() &&
	//	   					Math.abs(this.jogo.getMario().getX() - this.getXRelativo()) < this.jogo.getLarguraDaTela();
		boolean estaPerto = this.getYRelativo() < this.jogo.getAlturaDaTela() + 50 && this.getYMaxRelativo() > -50 &&
							this.getXRelativo() < this.jogo.getLarguraDaTela() + 50 && this.getXMaxRelativo() > -50;

		
		if(!this.encerrado) {
			if(!this.ativo && estaPerto) {
				this.ativar();
			} else if(this.ativo && !estaPerto) this.desativar();
		}
	}
	
	public void resetar() {
		this.moverParaDireita = true;
		this.estado = 0;
		this.x = this.oriPos.x;
		this.y = this.oriPos.y;
		this.width = 20;
		this.height = 32;
		this.imagemAtual = 0;
		this.vel = 1;
		this.timer1.setDelay(150);
		this.encerrado = false;
	}
}
