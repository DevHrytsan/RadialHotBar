package github.devhrytsan.radialhotbar.utils;

public class MathUtils {

	public static float relativeAngle(float xA, float yA, float xB, float yB) {
		float atan2 = (float)Math.atan2(yB - yA, xB - xA);
		float angle = (float)Math.toDegrees(atan2);
		//angle = 360 - angle;

		return normalizeAngle(angle);
	}

	public static boolean isAngleBetween(float target, float start, float end) {
		target = normalizeAngle(target);
		start = normalizeAngle(start);
		end = normalizeAngle(end);

		// Determine if the range wraps around the 0/360
		if (start < end) {
			return target >= start && target < end;
		} else {
			return target >= start || target < end;
		}
	}

	public static float normalizeAngle(float angle) {
		angle %= 360f;
		if (angle < 0) {
			angle += 360f;
		}
		return angle;
	}

	public static float calculateDistanceBetweenPoints(
			float x1,
			float y1,
			float x2,
			float y2) {
		return (float)Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
	}
	public static boolean betweenTwoValues(float variable, float minValueInclusive, float maxValueInclusive) {
		return variable >= minValueInclusive && variable <= maxValueInclusive;
	}
}
