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

import java.awt.Point;

import componentes.ObjetoAnimado;

import jogo.SMPropulsion;

public abstract class Inimigo extends ObjetoAnimado
{
	public int cobertura;
	public int xIncial;
	
	public Inimigo(int cobertura, SMPropulsion jogo, Point p1, Point p2)
	{
		super(jogo, p1, p2);
		this.cobertura = cobertura;
		xIncial = p1.x;
	}
	
	public void testarColisoesCom(ObjetoAnimado sprite)
	{
		if(ativo)
			if(colisaoAEsquerdaCom(sprite))
			{
				if(jogo.getMario().getEstado() == ESTADO.SEM_ITEM &&
				   !jogo.getMario().imortal)
				{
					//gameOver
				//	fase.posicaoCenario.x = ponto1.x + 14;
					jogo.getMario().vDireita = 0;
				}
				else
				{
					jogo.getMario().piscar();
				}
			}
			else if(colisaoADireitaCom(sprite))
			{
				if(jogo.getMario().getEstado() == ESTADO.SEM_ITEM &&
				   !jogo.getMario().imortal)
				{
					//gameOver
				//	fase.posicaoCenario.x = ponto2.x - 14;
					jogo.getMario().vEsquerda = 0;
				}
				else
				{
					jogo.getMario().piscar();
				}
			}
			else if(pisadoPor(sprite))
			{
				jogo.getMario().kickar();
				desativar();
			}
	}
	
	public abstract void ativar();
	public abstract void desativar();

}
