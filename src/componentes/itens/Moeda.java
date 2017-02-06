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

import componentes.ObjetoAnimado;
import jogo.SMPropulsion;

public class Moeda extends ObjetoAnimado {
	private static final long serialVersionUID = 1L;
	private ImageIcon imagens[];
	private File som;
	
	private static Animador animador;
	private static Timer coinTimer;
	private static int imgAtual;
	
	private class Animador implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Moeda.imgAtual = (Moeda.imgAtual + 1) % 4;
		}
	}

	public Moeda(SMPropulsion jogo, Point p) {
		super(jogo, p, new Point(p.x + 12, p.y - 16));
		
		this.imagens = new ImageIcon[4];
		for(int i = 0; i < this.imagens.length; ++i)
			this.imagens[i] = new ImageIcon("img/objetos/coin" + (i+1) + ".png");
		
		this.som = new File("sound/wav/coin.wav");
		this.ativo = true;
		
		if(Moeda.coinTimer == null) {
			Moeda.animador = new Animador();
			Moeda.coinTimer = new Timer(150, Moeda.animador);
			Moeda.coinTimer.start();
		}
	}

	public void desenhar(Graphics g) {
		this.imagens[Moeda.imgAtual].paintIcon(this.jogo, g, this.getXRelativo(), this.getYRelativo());
		
		if(SMPropulsion.DEBUG1_MOSTRAR_CONTORNO)
			this.jogo.desenharContorno(g, this.getXRelativo(), this.getYRelativo(), width, height);
	}

	public void testarColisoesCom(ObjetoAnimado sprite) {
		if(this.colidiu(sprite)){
			this.jogo.getMario().moedas(1);
			this.encerrado = true;
			this.jogo.reproduzirEfeitoSonoro(this.som);
			this.desativar();
		}
	}
	
	public void ativar() {
		this.ativo = true;
	}
	
	public void desativar() {
		this.ativo = false;
	}
	
	public void atualizar() {}
		
	@Override
	public void moverNaHorizontal(int vel) {
		// TODO Auto-generated method stub
		
	}
	
	public void resetar() {
		this.encerrado = false;
		this.ativo = true;
	}
}
