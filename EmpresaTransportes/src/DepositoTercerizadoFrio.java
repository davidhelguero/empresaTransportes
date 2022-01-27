
public class DepositoTercerizadoFrio extends Deposito{
	private double costoPorTonelada;
	
	DepositoTercerizadoFrio(double capacidad, double costoPorTonelada){
			super(capacidad,true,false);
			setCostoPorTonelada(costoPorTonelada);
	}
	
	private void setCostoPorTonelada(double costoPorTonelada) {
		if (costoPorTonelada > 0)
			this.costoPorTonelada = costoPorTonelada;
		else
			throw new RuntimeException("El costo debe ser mayor a 0");
	}
	
	public double getCostoPorTonelada() {
		return costoPorTonelada;
	}
	
	public String toString() {
		StringBuilder cad = new StringBuilder(super.toString());
		cad.append("\nCosto por tonelada: $");
		cad.append(costoPorTonelada);
		return cad.toString();
	}
	
}
