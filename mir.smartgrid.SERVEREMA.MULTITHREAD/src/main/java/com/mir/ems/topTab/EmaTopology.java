package com.mir.ems.topTab;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import javax.swing.JPanel;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import com.mir.ems.globalVar.global;

@SuppressWarnings("serial")
public class EmaTopology extends JPanel {
	public static int flag = 0;

	Graph graph = new SingleGraph("Tutorial 1");
	public final String emsEdge = global.SYSTEM_ID;

	public EmaTopology() {
		/*
		 * Graph Layout Setting
		 */
		java.net.URL emsUrl = EmaTopology.class.getResource("/IMAGE/dddd.png");

		System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
		// viewer.enableAutoLayout();
		viewer.disableAutoLayout();

		ViewPanel topologyPanel = (ViewPanel) viewer.addDefaultView(false);
		topologyPanel.setSize(1467, 700);
		add(topologyPanel);

		setBounds(14, 60, 1467, 700);
		setLayout(new BorderLayout(0, 0));
		setVisible(true);

		Node a = graph.addNode(emsEdge);

		a.addAttribute("ui.label", a.getId());

		int sum = 0;

		for (int i = 0; i < 20; i++) {

			sum += (i * 20);
		}

		a.setAttribute("x", (sum / 40));
		a.setAttribute("y", 10);

		a.addAttribute("ui.style",
				"text-alignment: above; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
						+ emsUrl + "');");

		createTopology();

	}

	public void createTopology() {
		java.net.URL gatewayUrl = EmaTopology.class.getResource("/IMAGE/GATEWAYICONS.png");
		java.net.URL ledUrl = EmaTopology.class.getResource("/IMAGE/LED.png");
		TimerTask chartUpdaterTask = new TimerTask() {

			Node emaGroup = null;
			Node deviceGroup = null;

			@Override
			public void run() {

				String arr;
				String[] parseArr;

				// ConcurrentHashMap<String, ServerEmaProfile> globalEmaListDup
				// = global.getOpenADRHashMap();

				ArrayList<String> emaString = new ArrayList<String>();
				emaString.addAll(global.emaProtocolCoAP.keySet());
				// emaString.addAll(global.openADRHashMap.keySet());

				String[] emaList = new String[emaString.size()];

				for (int i = 0; i < emaString.size(); i++) {
					emaList[i] = emaString.get(i).toString();
				}

				Arrays.sort(emaList);

				ArrayList<String> devString = new ArrayList<String>();
				// devString.addAll(global.devHashMap.keySet());
				devString.addAll(global.emaProtocolCoAP_Device.keySet());

//				int[] devList = new int[devString.size()];
				String[] devList = new String[devString.size()];

//				for (int i = 0; i < devString.size(); i++) {
//					devList[i] = Integer.parseInt(devString.get(i).toString());
//				}
				for (int i = 0; i < devString.size(); i++) {
					devList[i] = devString.get(i).toString();
				}
				
				
				Arrays.sort(devList);

				String nodeSet_ADD = graph.getNodeSet().toString();
				nodeSet_ADD = nodeSet_ADD.replace("[", "");
				nodeSet_ADD = nodeSet_ADD.replace("]", "");
				nodeSet_ADD = nodeSet_ADD.replace(" ", "");

				String[] nodeSet_ADD_ARR = nodeSet_ADD.split(",");
				Set<String> strSet = Arrays.stream(nodeSet_ADD_ARR).collect(Collectors.toSet());

				// int cnt = 0;
//				int devCnt = 0;

				// System.out.println("여기");

				for (int i = 0; i < emaList.length; i++) {
					String key = emaList[i];
					// System.out.println("리스트는 들어왓다");
					if (!strSet.contains(key.toString())) {

						try {
							emaGroup = graph.addNode(key);
							emaGroup.setAttribute("x", (global.cnt * 10));
							emaGroup.addAttribute("ui.label", emaGroup.getId());

							emaGroup.setAttribute("y", 0);
							emaGroup.addAttribute("ui.style",
									"text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
											+ gatewayUrl + "');");

							graph.addEdge(emsEdge + key, emsEdge, key);
							global.cnt += 1;
							// System.out.println("추가햇고");
						} catch (Exception e) {
						}
					}
				}

				String nodeDevSet_ADD = graph.getNodeSet().toString();
				nodeDevSet_ADD = nodeDevSet_ADD.replace("[", "");
				nodeDevSet_ADD = nodeDevSet_ADD.replace("]", "");
				nodeDevSet_ADD = nodeDevSet_ADD.replace(" ", "");

				String[] nodeDevSet_ADD_ARR = nodeDevSet_ADD.split(",");
				Set<String> strDevSet = Arrays.stream(nodeDevSet_ADD_ARR).collect(Collectors.toSet());

				int devCounting = devList.length;

//				if (devList.length > 0) {
//					if (devList[0] == 1) {
//						devCounting = 1;
//					} else {
//						devCounting = 5;
//
//					}
//				}
//				//
//				// if (devCounting > 5)
//				// devCounting = 5;

				for (int k = 0; k < devCounting; k++) {

					// System.out.println("여기");

					String dev_key = devList[k] + "";
					if (!strDevSet.contains(dev_key.toString())) {

						global.devCnt += 1;

						try {
							deviceGroup = graph.addNode(dev_key);
							deviceGroup.setAttribute("x", (global.devCnt * 10) - 5);
							deviceGroup.addAttribute("ui.label", deviceGroup.getId());
							deviceGroup.setAttribute("y", -20);
							deviceGroup.addAttribute("ui.style",
									"text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
											+ ledUrl + "');");

							graph.addEdge("DEVICE" + dev_key,
									global.emaProtocolCoAP_Device.get(dev_key).getEmaID().toString(), dev_key);

						} catch (Exception e) {
						}
					}

					if (global.emaProtocolCoAP_Device.get(dev_key).getState().matches("ON|on|On|oN")) {
						// System.out.println("온");
						graph.getEdge("DEVICE" + dev_key).addAttribute("ui.style", "fill-color: black;");

					} else if (global.emaProtocolCoAP_Device.get(dev_key).getState().matches("OFF|off|Off|oFF")) {
						// System.out.println("오프");
						graph.getEdge("DEVICE" + dev_key).addAttribute("ui.style", "fill-color: red;");

					}

				}

				// if (global.getEmaProtocolCoAP().get(key) != null) {
				//// System.out.println("그러면여기는?");
				// if
				// (global.getEmaProtocolCoAP().get(key).getConnect().equals("CONNECT")
				// && flag == 0) {
				//
				// // flag += 1;
				//
				// graph.getEdge(emsEdge + key).addAttribute("ui.style",
				// "fill-color: black;");
				// else if
				// (global.getEmaProtocolCoAP().get(key).getState().equals("DISCONNECT"))
				// {
				// graph.getEdge(emsEdge + key).addAttribute("ui.style",
				// "fill-color: red;");
				//
				// for (int j = 0; j < devList.length; j++) {
				// String dev_key = devList[j] + "";
				// graph.getEdge("DEVICE" + dev_key).addAttribute("ui.style",
				// "fill-color: red;");
				// }

				// }
				// }
				// } catch (Exception e) {
				// System.out.println("에러나는것");
				// }

				// }

			}

		};
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(chartUpdaterTask, 10000, 3000);
	}

}
