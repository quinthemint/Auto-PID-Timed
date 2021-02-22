package RobotLib.Swerve;

public interface Interpolable<T> {
    T interpolate(T other, float t);
}
