package dz.nouri.processing;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.neuroph.core.NeuralNetwork;

public final class AnimationCalculation {

	private static String animationSequece = "";

	private static String getContent(File file) {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			return reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static void saveFile(String content) {
		try (PrintWriter writer = new PrintWriter(new File(
				"./resultatExpression/expression.nouri"))) {
			writer.println(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static ArrayList<Point> getPointsCoord(File file) {

		ArrayList<Point> res = new ArrayList<>();

		String content = getContent(file);
		StringTokenizer fileToken = new StringTokenizer(content, "|");
		ArrayList<String> data = new ArrayList<>();

		while (fileToken.hasMoreTokens())
			data.add(fileToken.nextToken());

		for (int i = 0; i < data.size(); i++) {
			StringTokenizer pointToken = new StringTokenizer(data.get(i), ",");
			ArrayList<String> pointData = new ArrayList<>();
			while (pointToken.hasMoreTokens()) {
				pointData.add(pointToken.nextToken());
			}

			res.add(new Point(Integer.parseInt(pointData.get(0)), Integer
					.parseInt(pointData.get(1))));
		}

		return res;

	}

	private static String calculateAnimationParameters(
			ArrayList<Point> points1, ArrayList<Point> points2) {

		NeuralNetwork network = NeuralNetwork.load("./source/emotion.nnet");

		network.setInput(au1(points1, points2), au2(points1, points2),
				au4(points1, points2), au10(points1, points2),
				au12(points1, points2), au15(points1, points2),
				au20(points1, points2), au24(points1, points2),
				au26(points1, points2));
		network.calculate();
		String[] emotion = { "Neutre", "Peur", "Surprise", "Colere",
				"Tristesse", "Degout", "Joie" };
		double max = network.getOutput()[0];
		int index = 0;
		for (int i = 1; i < network.getOutput().length; i++)
			if (network.getOutput()[i] > max) {
				max = network.getOutput()[i];
				index = i;
			}

		return emotion[index];

	}

	private static int au1(final ArrayList<Point> pointList1,
			final ArrayList<Point> pointList2) {

		final int i = 13, j = 14;

		Point p1 = pointList1.get(i);
		Point p2 = pointList1.get(j);
		Point p3 = pointList2.get(i);
		Point p4 = pointList2.get(j);

		if ((p1.y - p3.y) > 0 && (p2.y - p4.y) > 0)
			return 1;
		else
			return 0;
	}

	private static int au2(final ArrayList<Point> pointList1,
			final ArrayList<Point> pointList2) {

		final int i = 12, j = 15;

		Point p1 = pointList1.get(i);
		Point p2 = pointList1.get(j);
		Point p3 = pointList2.get(i);
		Point p4 = pointList2.get(j);

		if ((p1.y - p3.y) > 0 && (p2.y - p4.y) > 0)
			return 1;
		else
			return 0;
	}

	private static int au4(final ArrayList<Point> pointList1,
			final ArrayList<Point> pointList2) {

		final int i = 13, j = 14;

		Point p1 = pointList1.get(i);
		Point p2 = pointList1.get(j);
		Point p3 = pointList2.get(i);
		Point p4 = pointList2.get(j);

		if ((p1.y - p3.y) > 0 && (p2.y - p4.y) > 0)
			return 0;
		else
			return 1;
	}

	private static int au10(final ArrayList<Point> pointList1,
			final ArrayList<Point> pointList2) {

		final int i = 54;

		Point p1 = pointList1.get(i);

		Point p2 = pointList2.get(i);

		if ((p1.y - p2.y) > 0)
			return 1;
		else
			return 0;
	}

	private static int au12(final ArrayList<Point> pointList1,
			final ArrayList<Point> pointList2) {

		final int i = 3, j = 4;

		Point p1 = pointList1.get(i);
		Point p2 = pointList1.get(j);
		Point p3 = pointList2.get(i);
		Point p4 = pointList2.get(j);

		if ((p1.y - p3.y) > 0 && (p2.y - p4.y) > 0)
			return 1;
		else
			return 0;
	}

	private static int au15(final ArrayList<Point> pointList1,
			final ArrayList<Point> pointList2) {

		final int i = 3, j = 4;

		Point p1 = pointList1.get(i);
		Point p2 = pointList1.get(j);
		Point p3 = pointList2.get(i);
		Point p4 = pointList2.get(j);

		if ((p1.y - p3.y) > 0 && (p2.y - p4.y) > 0)
			return 1;
		else
			return 0;
	}

	private static int au20(final ArrayList<Point> pointList1,
			final ArrayList<Point> pointList2) {

		final int i = 3, j = 4;

		Point p1 = pointList1.get(i);
		Point p2 = pointList1.get(j);
		Point p3 = pointList2.get(i);
		Point p4 = pointList2.get(j);

		if ((p1.x - p3.x) > 0 && (p2.x - p4.x) < 0)
			return 1;
		else
			return 0;
	}

	private static int au24(final ArrayList<Point> pointList1,
			final ArrayList<Point> pointList2) {

		final int i = 3, j = 4;

		Point p1 = pointList1.get(i);
		Point p2 = pointList1.get(j);
		Point p3 = pointList2.get(i);
		Point p4 = pointList2.get(j);

		if ((p1.x - p3.x) < 0 && (p2.x - p4.x) > 0)
			return 1;
		else
			return 0;
	}

	private static int au26(final ArrayList<Point> pointList1,
			final ArrayList<Point> pointList2) {

		final int i = 55;

		Point p1 = pointList1.get(i);
		Point p3 = pointList2.get(i);

		if ((p1.y - p3.y) > 0)
			return 0;
		else
			return 1;
	}

	public static void calculateAnimationSquence() {

		File folder = new File("./data");

		for (int i = 0; i < folder.listFiles().length - 1; i++) {
			ArrayList<Point> pointList1 = getPointsCoord(folder.listFiles()[i]);
			ArrayList<Point> pointList2 = getPointsCoord(folder.listFiles()[i + 1]);
			animationSequece += calculateAnimationParameters(pointList1,
					pointList2) + "|";
		}

		saveFile(animationSequece);

	}

}
