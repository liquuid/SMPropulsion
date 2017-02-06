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
import jogo.SMPropulsion;
import plataformas.Plataforma;
import componentes.ObjetoAnimado;

/* 
 * 					O_________________\ eixo X
 *					|				  /
 *   limiteSuperior |-----------------| (padrao = 220)
 *                  |				  |
 *  	alturaFinal |.................| (padrao = alturaDaTela / 2)
 *  				|				  |					
 *    limiteInfeior	|-----------------| (padrao = 300)
 *  				|				  |
 *     		       \|/________________|
 *					v
 *				  eixo Y
 */	


public class CorretorVertical implements Runnable, Animacao
{
	private Plataforma plataforma;
	private ObjetoAnimado personagem;
	private int deslocamento, condicao, alturaFinal;
	private boolean deslocar;
	private SMPropulsion jogo;
	
	public CorretorVertical(SMPropulsion jogo, Plataforma plataforma, ObjetoAnimado personagem, int altFinal)
	{
		this.plataforma = plataforma;
		this.personagem = personagem;
		this.jogo = jogo;
		this.alturaFinal = altFinal;
		
		new Thread(this).start();
	}
	
	private boolean condicao1()
	{
		return (this.personagem.getMaxY() > this.alturaFinal && jogo.getFaseAtual().y() > 0);
	}
	
	private boolean condicao2()
	{
		return (this.personagem.getMaxY() < this.alturaFinal);
	}
	
	public void run()
	{
		//this.personagem.moverParaDireita = false;
		//this.personagem.moverParaEsquerda = false;
		//this.personagem.controlavel = false;
		
		if(condicao1()) {
			this.deslocamento = -1;
			this.condicao = 1;
			this.loop();
		} else if(condicao2()) {
			this.deslocamento = 1;
			this.condicao = 2;
			this.loop();
		}
		
		//this.personagem.controlavel = true;
	}

	public void atualizar() {
		jogo.getFaseAtual().y(jogo.getFaseAtual().y() + (deslocamento));
		jogo.getFaseAtual().moverFundoNaVertical(deslocamento, 1);
		this.personagem.y = this.plataforma.getYRelativo() - this.personagem.height;
		
		if(this.condicao == 1) this.deslocar = this.condicao1();
		else this.deslocar = this.condicao2();
	}

	public void desenhar(Graphics2D gScr) {}

	public void loop() {
		Temporizador temp = new Temporizador(this, 8);
		this.deslocar = true;
		
		while(this.deslocar)
		{
			this.atualizar();
			temp.normalizar();
			temp.pularQuadros();
		}
	}
}