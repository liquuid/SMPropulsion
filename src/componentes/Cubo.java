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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.Timer;

import componentes.itens.Cogumelo;



import jogo.SMPropulsion;

public class Cubo extends Quadrado implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private Cogumelo cogumelo;
	
	protected ImageIcon[] imagensCubo;
	protected int imagemAtual = 0;
	protected Timer timer;
	protected File som2_item;

	public Cubo(SMPropulsion jogo, Point p1, Point p2, String img, int numImgs)
	{
		super(jogo, p1, p2, null);
				
		imagensCubo = new ImageIcon[numImgs];

		this.som2_item = new File("sound/wav/item.wav");
		
		for(int i = 0; i < numImgs; ++i)
			imagensCubo[i] = new ImageIcon("img/objetos/" + img + i + ".png");
	}
	
	public Cubo(SMPropulsion jogo, Point p)
	{
		this(jogo, p, new Point(p.x + 16, p.y - 16), "cubo", 5);
		
		cogumelo = new Cogumelo(this, jogo, new Point(p));
		
		timer = new Timer(150, this);
		timer.start();
	}
		
	public void desenhar(Graphics g)
	{
		if(cogumelo != null && cogumelo.ativo) cogumelo.desenhar(g);
		
		imagensCubo[imagemAtual].paintIcon(jogo, g, this.getXRelativo(), this.getYRelativo());
	
		if(SMPropulsion.DEBUG1_MOSTRAR_CONTORNO)
			this.jogo.desenharContorno(g, this.getXRelativo(), this.getYRelativo(), width, height);
	}

	public void testarColisoesCom(ObjetoAnimado sprite)
	{
		if(this.cogumelo != null && this.cogumelo.ativo)
			this.cogumelo.testarColisoesCom(sprite);
		
		if(colisaoAEsquerdaCom(sprite))
		{
			sprite.vDireita = 0;
			sprite.saltando = false;
		}
		else if(colisaoADireitaCom(sprite))
		{
			sprite.vEsquerda = 0;
			sprite.saltando = false;
		}
		else if(colidiuEmBaixoCom(sprite))
		{
			sprite.vVertical = -sprite.vVertical;

			if(timer.isRunning())
			{
				this.y -= 5;
				new Timer(100, new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Cubo.this.y += 5;
						((Timer)e.getSource()).stop();
					}
				}).start();
				
				timer.stop();
				imagemAtual = 4;
				this.jogo.reproduzirEfeitoSonoro(this.som2_item);
				cogumelo.ativar();
				this.encerrado = true;
			}
			else
				this.jogo.reproduzirEfeitoSonoro(this.som1_tock);
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(timer.isRunning())
			imagemAtual = (imagemAtual + 1) % 4;
	}
	
	public void resetar() {
		if(cogumelo != null) this.cogumelo.resetar();
		if(timer != null) timer.start();
		this.imagemAtual = 0;
		this.encerrado = false;
	}
	
	public void ativar() {
		this.ativo = true;
		if(!this.encerrado && timer != null && !timer.isRunning()) timer.start();
	}
	
	public void desativar() {
		this.ativo = false;
		if(timer != null && timer.isRunning()) timer.stop();
	}
	
	public void testarAtivacao() {
		super.testarAtivacao();
		if(this.cogumelo != null && this.cogumelo.ativo) this.cogumelo.atualizar();
	}
}
