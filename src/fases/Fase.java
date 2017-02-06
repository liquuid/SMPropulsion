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

package fases;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.Timer;
import componentes.ObjetoAnimado;
import componentes.ObjetoTangivel;
import etc.Relogio;
import plataformas.Plataforma;
import jogo.SMPropulsion;

public abstract class Fase
{
	public class Listener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Fase.this.mostrarIntro = false;
			Fase.this.timer1.stop();

			System.gc();
		}
	}
	
	public SMPropulsion jogo;
	protected int x, y, forcaDaGravidade = -2, comprimento = 200, limSupMario = 130, limInfMario = 320;
	public Point pos1F, pos2F, pos1FC1, pos2FC1,
				 pos1FC2, pos2FC2;
	protected boolean ativa = true, gravidade = true, mostrarIntro = true, mostrarGameOver = false;
	protected Timer timer1;
	protected Relogio relogio;
	protected File musica;
	protected ArrayList<Plataforma> cenario;
	protected ArrayList<ObjetoAnimado> objetos;
		
	public int tempoDeTurno;
	public abstract void atualizar();
	public abstract void instanciarComponentes();
	public abstract void inicializarPosicoes();
	public abstract void moverFundoNaHorizontal(int v);
	public abstract void moverFundoNaVertical(int v, int escala);
	public abstract int getLimiteEsquerdo();
	public abstract int getLimiteDireito();
	public abstract void desenharFundo(Graphics g);
	public abstract void desenharComponentes(Graphics g);
	public abstract void iniciarMusica();
	
	public Plataforma getPlataforma(int i) //?
	{
		return cenario.get(i);
	}
	
	public ObjetoAnimado getPersonagem(int i) //apagar
	{
		return objetos.get(i);
	}
	
	public ArrayList<ObjetoAnimado> getObjetos() { return this.objetos; }
	
	public void gameOver(boolean go) { this.mostrarGameOver = true; }
	
	public boolean ativa() { return this.ativa; }
	public void ativa(boolean a) { this.ativa = a; }
	
	public int limSupMario() { return this.limSupMario; }
	public void limSupMario(int l) { this.limSupMario = l; }
	
	public int limInfMario() { return this.limInfMario; }
	public void limInfMario(int l) { this.limInfMario = l; }
	
	public int forcaDaGravidade() { return this.forcaDaGravidade; }
	public void forcaDaGravidade(int f) { this.forcaDaGravidade = f; }
	
	public int comprimento() { return this.comprimento; }
	public void comprimento(int c) { this.comprimento = c; }
	
	public void testarColisaoPara(ObjetoAnimado personagem)
	{
		//colisao com elementos estaticos
		for(Plataforma i: cenario)
			if(i.ativo) i.testarColisoesCom(personagem);
		
		//colisao com outros personagens
		for(ObjetoAnimado i: objetos)
			if(i.ativo) i.testarColisoesCom(personagem);
	}
	
	public boolean pisouEmPlataforma(ObjetoAnimado personagem)
	{
		for(Plataforma i: cenario)
			if(i.ativo)
				if(i.testarChaoPara(personagem))
					return true;
		
		return false;
	}
	
	public void inicializar() {
		this.inicializarPosicoes();
		
		this.jogo.pararMusica();
		this.iniciarMusica();
		
		this.gravidade(true);
		this.mostrarGameOver = false;
		this.mostrarIntro = true;
		this.timer1.start();
		this.resetarObjetos();
		this.resetarCenatio();
		//this.jogo.zerarTempo();
	}
	
	public void resetarObjetos() {
		for(ObjetoTangivel i : this.objetos)
			i.resetar();
	}
	
	public void resetarCenatio() {
		for(ObjetoTangivel i : this.cenario)
			i.resetar();
	}
	
	public void reiniciar() {
		this.jogo.getMario().inicializar();
		this.resetarObjetos();
		this.resetarCenatio();
		this.inicializar();
		
		System.gc();
	}

	public boolean gravidade() { return this.gravidade; }
	
	public void gravidade(boolean g) {
		this.gravidade = g;
		
		if(!g) this.limSupMario(260);
		else this.limSupMario(130);
	}
	
	public int x() { return this.x; }
	public int y() { return this.y; }
	public void x(int x) { this.x = x; }
	public void y(int y) { this.y = y; }
}