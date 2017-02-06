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

package intro;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.Serializable;

public class DadosRecords implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String lista[][] = new String[6][2];

	private Font fonte = new Font("Arial", Font.PLAIN, 12);
	
	public void criarNovaLista()
	{
		for(int lin = 0; lin < lista.length; ++lin)
		{
			lista[lin][0] = "...";
			lista[lin][1] = "0";
		}
	}
	
	public void inserirNovoDado(String name, String points)
	{
		String tempName, tempPoints;
		
		for(int lin = 0; lin < lista.length; ++lin)
			if(Integer.parseInt(points) > Integer.parseInt(lista[lin][1]))
			{
				if(lin == lista.length - 1)
				{
					lista[lin][0] = name;
					lista[lin][1] = points;
				}
				else
				{
					for(int l = lin; l < lista.length; ++l)
					{
						tempName = lista[l][0];
						tempPoints = lista[l][1];
						lista[l][0] = name;
						lista[l][1] = points;
						name = tempName;
						points = tempPoints;
					}
				}
				
				break;
			}
	}
	
	public boolean novoRecord(int points) {
		for(int i = 0; i < this.lista.length - 2; ++i)
			if(points > Integer.parseInt(this.lista[i][1]))
				return true;
		
		return false;
	}
	
	public void desenharRecords(Graphics g)
	{
		g.setColor(Color.WHITE);
		
		g.setFont(fonte);
		for(int lin = 0; lin < lista.length - 1; ++lin)
			for(int col = 0; col < lista[lin].length; ++col)
				if(col == 0)
					g.drawString(String.format("%d %s", lin + 1, lista[lin][col]), 100 + 2 * 20, 267 + 11 * (lin));
				else
					g.drawString(String.format("%s pontos", lista[lin][col]), 170 + 6 * 20, 267 + 11 * (lin));
	}
	
	
}
