package demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.bson.Document;

public class PersonThroughRiver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int n;
		List<Integer> times = new ArrayList<Integer>();
		Scanner sc = new Scanner(System.in);
		System.out.println("请输入有多少个人要过桥:");
		n = sc.nextInt();
		System.out.println("请输入每个过桥的时间，空格隔开");
		for (int i = 0; i < n; i++) {
			int _time;
			_time = sc.nextInt();
			times.add(_time);
		}
		int [] mark = new int[1 << n];
		for (int i = 0; i < (1 << n); i++) {
			mark[i] = Integer.MAX_VALUE;
		}
		mark[(1 << n) - 1] = 0;
		List<String> record = new ArrayList<String>();
		List<Integer> int_record = new ArrayList<Integer>();
		for (int i = 0; i < (1 << n) - 1; i++) {
			String tmp = "";
			record.add(tmp);
			int tmpint = 0;
			int_record.add(tmpint);
		}
		int cnt = 0;
		for (int i = (1 << n) - 1; i >= 0; i--) {
			cnt++;
			//然后开始随机选择两个人
			for (int i1 = 0; i1 < n; i1++) {
				if (((1 << i1)&i) == 0) {
					continue;
				}
				for (int i2 = i1 + 1; i2 < n; i2++) {
					if (((1 << i2)&i) == 0) {
						continue;
					}
					
					//i1,i2都还没过河
					int j = (((1 << n) - 1)^i)|(1<<i1)|(1<<i2);//已经过河的人
					int time = Math.max(times.get(i1), times.get(i2));
					if (((1 << i1)|(1 << i2)) == i) {
						int now_mark = mark[i] + time;
						if (now_mark < mark[0]) {
							mark[0] = now_mark;
							String path = "第" + cnt + "轮," + (char)('A' + i1) + "," +
										  (char)('A' + i2) + "一起过河";
							record.set(0, path);
							int_record.set(0, i);
						}
						continue;
					}
					int timei = times.get(i1) + times.get(i2);//这轮过河的时间和
					for (int j1 = 0; j1 < n; j1++) {
						if (((1 << j1)&j) == 0) {
							continue;
						}
						if (timei - times.get(j1) > 0) {//只有未过河的时间和变小了，才处理
							int lasti = (i^(1<<i1)^(1<<i2))|(1<<j1);
							int now_mark = mark[i] + time + times.get(j1);
							if (now_mark < mark[lasti]){
								mark[lasti] = now_mark;
								String path = "第" + cnt + "轮," + (char)('A' + i1) + "," +
										  (char)('A' + i2) + "一起过河" + "\n\t" + (char)('A' + j1) + "拿着手电筒返回";
								record.set(lasti, path);
								int_record.set(lasti, i);
							}
							//mark[lasti] = Math.min(mark[lasti], mark[i] + time + times.get(j1));
						}
					}
				}
			}
		}
		
		System.out.println("最短过河时间为: " + mark[0]);
		System.out.println("具体的过河方案如下:");
		List<String> ans = new ArrayList<String>();
		int tmp = 0;
		while(tmp != ((1<<n) - 1)) {
			ans.add(record.get(tmp));
			tmp = int_record.get(tmp);
		}
		for (int i = ans.size() - 1; i >= 0; i--) {
			System.out.println(ans.get(i));
		}
	}

}

