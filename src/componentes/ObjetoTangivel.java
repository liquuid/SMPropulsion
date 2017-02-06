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
import java.awt.Rectangle;

import personagens.Mario;
import jogo.SMPropulsion;

public abstract class ObjetoTangivel extends Rectangle
{
	public boolean ativo = true, encerrado = false;
	protected SMPropulsion jogo;
	protected ObjetoAnimado personagemATratar;
	
	public ObjetoTangivel(SMPropulsion jogo, Point p1, Point p2)
	{
		this.jogo = jogo;
		
		this.x = p1.x;
		this.y = p1.y;
		
		this.width = Math.abs(p1.x - p2.x);
		this.height = Math.abs(p1.y - p2.y);
	}
	
	public int getXRelativo()
	{
		//return (this instanceof Mario) ? this.x : this.x + jogo.getFaseAtual().x;
		return this.x + jogo.getFaseAtual().x();
	}
	
	public int getXMaxRelativo()
	{
		return (int) this.getMaxX() + jogo.getFaseAtual().x();
	}
	
	public int getYRelativo()
	{
		return this.y + jogo.getFaseAtual().y();
	}	
	
	public int getYMaxRelativo()
	{
		return (int) this.getMaxY() + jogo.getFaseAtual().y();
	}
	
	public abstract void testarColisoesCom(ObjetoAnimado sprite);
		
	protected boolean colidiu(ObjetoAnimado sprite)
	{
		int xPersonagem = (sprite instanceof Mario) ? sprite.x : sprite.getXRelativo();
		int yPersonagem = (sprite instanceof Mario) ? ((int) sprite.getMaxY()) : sprite.getYMaxRelativo();
		
		return
		xPersonagem + sprite.width >= getXRelativo() &&
		xPersonagem <= getXMaxRelativo() &&
		yPersonagem >= getYRelativo() + 5 &&
		yPersonagem - sprite.height <= getYMaxRelativo() - 5;
	}
		
	protected boolean colisaoAEsquerdaCom(ObjetoAnimado sprite)
	{
		int xPersonagem = (sprite instanceof Mario) ? sprite.x : sprite.getXRelativo();
		
		if(colidiu(sprite))
			if(xPersonagem + sprite.width <= getXRelativo() + 10)
				return true;
			
		return false;
	}

	protected boolean colisaoADireitaCom(ObjetoAnimado sprite)
	{
		int xPersonagem = (sprite instanceof Mario) ? sprite.x : sprite.getXRelativo();
		
		if(colidiu(sprite))
			if(xPersonagem >= getXMaxRelativo() - 10)
				return true;
					
		return false;
	}
	
	protected boolean colidiuEmBaixoCom(ObjetoAnimado sprite)
	{
		int xPersonagem = (sprite instanceof Mario) ? sprite.x : sprite.getXRelativo();
		int yPersonagem = (sprite instanceof Mario) ? ((int) sprite.getMaxY()) : sprite.getYMaxRelativo();
		
		if(xPersonagem < getXMaxRelativo() &&
		   xPersonagem + sprite.width > getXRelativo() &&
		   yPersonagem > getYRelativo() &&
		   yPersonagem - sprite.height < getYMaxRelativo() + 2)
			if(yPersonagem - sprite.height >= getYMaxRelativo() - 2 &&
			   sprite.vVertical > 0)
				return true;
		
		return false;
	}
	
	protected boolean pisadoPor(ObjetoAnimado sprite)
	{
		boolean isMario = (sprite instanceof Mario);
		int xPersonagem = (isMario) ? sprite.x : sprite.getXRelativo();
		int yPersonagem = (isMario) ? ((int) sprite.getMaxY()) : sprite.getYMaxRelativo();
		
		return
		xPersonagem + sprite.width > getXRelativo() + 1 &&
		xPersonagem < getXMaxRelativo() - 1 &&
		yPersonagem >= getYRelativo() - 1 &&
		yPersonagem < getYRelativo() + 5 &&
		sprite.vVertical <= 0;
	}
	
	public boolean testarChaoPara(ObjetoAnimado sprite)
	{
		if(pisadoPor(sprite))
		{
			sprite.y = (sprite instanceof Mario ? getYRelativo() : this.y) - sprite.height;
			return true;
		}
		else return false;
	}
	
	public abstract void desenhar(Graphics g);
	
	public void testarAtivacao()
	{
		//boolean estaPerto = Math.abs(this.jogo.getMario().getY() - this.getYRelativo()) < this.jogo.getAlturaDaTela() + 50 &&
		//   					Math.abs(this.jogo.getMario().getX() - this.getXRelativo()) < this.jogo.getLarguraDaTela() + 50;
		
		boolean estaPerto = this.getYRelativo() < this.jogo.getAlturaDaTela() + 100 && this.getYMaxRelativo() > -100 &&
							this.getXRelativo() < this.jogo.getLarguraDaTela() + 100 && this.getXMaxRelativo() > -100;
		
		if(!this.encerrado) {
			if(!this.ativo && estaPerto) {
				this.ativar();
			} else if(this.ativo && !estaPerto) this.desativar();
		}
	}
	
	public abstract void resetar();
	
	public void ativar() { this.ativo = true; };
	public void desativar() { this.ativo = false; };
	//public abstract void atualizar(); //colocado em ObjetoAnimado
}
