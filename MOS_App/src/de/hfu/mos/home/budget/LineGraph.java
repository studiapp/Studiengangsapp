package de.hfu.mos.home.budget;

import java.util.ArrayList;
import java.util.Arrays;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer.FillOutsideLine;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import de.hfu.mos.R;


public class LineGraph extends Fragment{
	
public int counter = 0;	
public String toast = null;
public String beruf = null;
public ArrayList<Integer> coordinates;
public TextView header;
public int eigeneberb;
public int eigeneberm;

//@SuppressWarnings("deprecation")
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		XYSeriesRenderer renderer = new XYSeriesRenderer();
		XYSeriesRenderer renderer2 = new XYSeriesRenderer();
		FillOutsideLine fill1 = new FillOutsideLine(FillOutsideLine.Type.BOUNDS_ALL);
		FillOutsideLine fill2 = new FillOutsideLine(FillOutsideLine.Type.BOUNDS_ALL);


		if(counter == 0){
		//BACHELOR
		int[] x = {25,25,27,27,30,30,35,35,40,40,45,45,50,50,55,55,60,60,65,65};	
		TimeSeries series = new TimeSeries("Abschluß Bachelor ");
		for(int i = 0; i < x.length; i = i+2){
			series.add(x[i],coordinates.get(i));
			// Linienst�rke
			renderer.setLineWidth(5f);
			renderer.setPointStyle(PointStyle.SQUARE);
			renderer.setFillPoints(true);
		}				
		dataset.addSeries(series);		
		renderer.setColor(Color.GREEN);	
		fill1.setColor(Color.parseColor("#5500FF66"));
		renderer.addFillOutsideLine(fill1);
		//MASTER		
		int[] a = {25,25,27,27,30,30,35,35,40,40,45,45,50,50,55,55,60,60,65,65};
		//int[] b = {28,30,35,50,90,110};		
		TimeSeries series2 = new TimeSeries("Abschluß Master");
		for(int c = 1; c < a.length; c = c+2){
			series2.add(a[c],coordinates.get(c));
			//Linienst�rke
			renderer2.setLineWidth(5f);
			renderer.setChartValuesTextSize(50);
		}	
		dataset.addSeries(series2);		
		renderer2.setColor(Color.RED);	
		fill2.setColor(Color.parseColor("#55DB374A"));
		renderer2.addFillOutsideLine(fill2);
		
		} 
		// Eigene Berechnung Funktion
		else if(counter == 1){			
			// BACHELOR
			int[] x = {25,25,27,27,30,30,35,35,40,40,45,45,50,50,55,55,60,60,65,65};
			float eingangswertb = (float) eigeneberb;		
			TimeSeries series = new TimeSeries("Abschluß Bachelor ");
				series.add(25,eingangswertb);
			for(int i = 2; i < x.length; i = i+2){
				eingangswertb = (float) (eingangswertb * 1.2);
				series.add(x[i],eingangswertb);
				//Linienst�rke
				renderer.setLineWidth(5f);
				renderer.setPointStyle(PointStyle.SQUARE);
				renderer.setFillPoints(true);
			}				
			dataset.addSeries(series);		
			renderer.setColor(Color.GREEN);	
			fill1.setColor(Color.parseColor("#5500FF66"));
			renderer.addFillOutsideLine(fill1);
			// MASTER		
			int[] a = {25,25,27,27,30,30,35,35,40,40,45,45,50,50,55,55,60,60,65,65};
			float eingangswertm = (float) eigeneberm;	
			TimeSeries series2 = new TimeSeries("Abschluß Master");
			series2.add(25, 0);
			series2.add(27, eingangswertm);
			for(int c = 5; c < a.length; c = c+2){
				eingangswertm = (float) (eingangswertm * 1.2);
				series2.add(x[c],eingangswertm);;
				//Linienst�rke
				renderer2.setLineWidth(5f);
				renderer.setChartValuesTextSize(50);
			}	
			dataset.addSeries(series2);		
			renderer2.setColor(Color.RED);	
			fill2.setColor(Color.parseColor("#55DB374A"));
			renderer2.addFillOutsideLine(fill2);		
		} 
		else {	
			System.out.println("ERROR, wrong Paramater");
		}
	
		
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		mRenderer.addSeriesRenderer(renderer);
		mRenderer.addSeriesRenderer(renderer2);
		mRenderer.setChartTitle("");
		

		//overall graph settings
		//mRenderer.setChartTitle("Test");
		mRenderer.setLabelsTextSize(40);
		mRenderer.setAxisTitleTextSize(40);
		mRenderer.setXTitle("Alter in Jahren");
		mRenderer.setYTitle("Gehalt in €");
		//mRenderer.setLegendHeight(80);
		//renderer.setMargins(new int[] { top, left, bottom, right });
		mRenderer.setMargins(new int[] {0, 60, 90,0});		
		mRenderer.setLegendTextSize(30);
		mRenderer.setLegendHeight(60);
		
		View view = inflater.inflate(R.layout.dashboard_chart, container, false);		
		// Getting a reference to LinearLayout of the MainActivity Layout
        LinearLayout chartContainer = (LinearLayout)view.findViewById(R.id.chart_container);
        // Creating a Line Chart
        View mChart = ChartFactory.getLineChartView(getActivity().getBaseContext(), dataset, mRenderer);
        // Adding the Line Chart to the LinearLayout
        chartContainer.addView(mChart);	
        Toast.makeText(getActivity(), toast, TRIM_MEMORY_COMPLETE).show();
        //getActivity().setContentView(R.layout.dashboard_chart);
        header = (TextView) view.findViewById(R.id.tv_title);
        header.setText(beruf);
		return view;
}

	//Constructor
	public LineGraph(String selection, String toast, int number, Integer...ints) {
		this.beruf = selection;
		this.toast = toast;
		this.counter = number;
		this.coordinates = new ArrayList<Integer>(Arrays.asList(ints));	
		if(number == 1){
		this.eigeneberb = this.coordinates.get(0);
		this.eigeneberm = this.coordinates.get(1);
		}
	}


}
