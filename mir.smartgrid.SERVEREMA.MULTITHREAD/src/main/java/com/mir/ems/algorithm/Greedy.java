package com.mir.ems.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import com.mir.ems.globalVar.global;

public class Greedy {

	// TODO Auto-generated method stub

	public void start() {
		ArrayList<Integer> memoization = new ArrayList<Integer>();
		memoization.add(0);

		double minusVal = 0;

		int emaCount = global.emaProtocolCoAP.size();

		while (true) {
			double available = global.AVAILABLE_THRESHOLD;

			Iterator<String> it = global.emaProtocolCoAP.keySet().iterator();
			int cnt = 0;
			while (it.hasNext()) {

				String key = it.next();

				double threshold;
				double emaAvgValue = global.emaProtocolCoAP.get(key).getAvgValue();
				threshold = emaAvgValue - minusVal < 0 ? emaAvgValue : emaAvgValue - minusVal;
				available -= threshold;

				global.emaThresholdManage.replace(key, threshold);

				if (available <= 0) {
					break;
				}
				cnt += 1;

			}
			memoization.add(cnt);

			minusVal += 1;

			if (Collections.max(memoization) == emaCount) {

				// global.AVAILABLE_THRESHOLD = available;
				break;

			}

		}
	}

}
