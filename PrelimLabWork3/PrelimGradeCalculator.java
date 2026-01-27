import java.util.Scanner;

public class PrelimGradeCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Constants for grading breakdown
        final double ATTENDANCE_WEIGHT = 0.40;
        final double LABWORK_WEIGHT = 0.60;
        final double CLASS_STANDING_WEIGHT = 0.70;
        final double PRELIM_EXAM_WEIGHT = 0.30;
        final double PASSING_GRADE = 75.0;
        final double EXCELLENT_GRADE = 100.0;
        final int TOTAL_CLASSES = 12; // Assuming 12 total classes
        
        System.out.println("=".repeat(60));
        System.out.println("         PRELIM GRADE CALCULATOR");
        System.out.println("=".repeat(60));
        
        // Input: Number of attendances
        System.out.print("\nEnter number of attendances: ");
        int attendances = scanner.nextInt();
        
        // Input: Lab Work grades
        System.out.print("Enter Lab Work 1 grade: ");
        double labWork1 = scanner.nextDouble();
        
        System.out.print("Enter Lab Work 2 grade: ");
        double labWork2 = scanner.nextDouble();
        
        System.out.print("Enter Lab Work 3 grade: ");
        double labWork3 = scanner.nextDouble();
        
        // Validate inputs
        if (attendances < 0 || attendances > TOTAL_CLASSES) {
            System.out.println("\nError: Invalid number of attendances!");
            scanner.close();
            return;
        }
        
        if (labWork1 < 0 || labWork1 > 100 || 
            labWork2 < 0 || labWork2 > 100 || 
            labWork3 < 0 || labWork3 > 100) {
            System.out.println("\nError: Lab work grades must be between 0 and 100!");
            scanner.close();
            return;
        }
        
        // Formula 1: Lab Work Average
        double labWorkAverage = (labWork1 + labWork2 + labWork3) / 3.0;
        
        // Calculate Attendance Score (out of 100)
        double attendanceScore = (attendances / (double) TOTAL_CLASSES) * 100.0;
        
        // Formula 2: Class Standing
        double classStanding = (ATTENDANCE_WEIGHT * attendanceScore) + 
                               (LABWORK_WEIGHT * labWorkAverage);
        
        // Formula 3: Calculate required Prelim Exam score
        // Prelim Grade = (0.30 × Prelim Exam) + (0.70 × Class Standing)
        // Solving for Prelim Exam:
        // Prelim Exam = (Prelim Grade - 0.70 × Class Standing) / 0.30
        
        double requiredExamToPass = (PASSING_GRADE - (CLASS_STANDING_WEIGHT * classStanding)) 
                                    / PRELIM_EXAM_WEIGHT;
        
        double requiredExamToExcel = (EXCELLENT_GRADE - (CLASS_STANDING_WEIGHT * classStanding)) 
                                     / PRELIM_EXAM_WEIGHT;
        
        // Display Results
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                    RESULTS");
        System.out.println("=".repeat(60));
        
        System.out.printf("\nAttendance Score:          %.2f%% (%d out of %d classes)\n", 
                         attendanceScore, attendances, TOTAL_CLASSES);
        System.out.printf("Lab Work 1:                %.2f\n", labWork1);
        System.out.printf("Lab Work 2:                %.2f\n", labWork2);
        System.out.printf("Lab Work 3:                %.2f\n", labWork3);
        System.out.printf("Lab Work Average:          %.2f\n", labWorkAverage);
        System.out.printf("Class Standing:            %.2f\n", classStanding);
        
        System.out.println("\n" + "-".repeat(60));
        System.out.println("         REQUIRED PRELIM EXAM SCORES");
        System.out.println("-".repeat(60));
        
        // Display required exam score to pass
        System.out.printf("\nTo PASS (75%%):             %.2f\n", requiredExamToPass);
        
        if (requiredExamToPass > 100) {
            System.out.println("Remark: It is mathematically impossible to pass.");
            System.out.println("        Your current class standing is too low.");
        } else if (requiredExamToPass < 0) {
            System.out.println("Remark: You will pass regardless of your Prelim Exam score!");
        } else {
            System.out.printf("Remark: You need to score at least %.2f on the Prelim Exam to pass.\n", 
                            requiredExamToPass);
        }
        
        // Display required exam score for excellent grade
        System.out.printf("\nTo achieve EXCELLENT (100%%): %.2f\n", requiredExamToExcel);
        
        if (requiredExamToExcel > 100) {
            System.out.println("Remark: It is mathematically impossible to achieve an excellent grade.");
        } else if (requiredExamToExcel <= 100 && requiredExamToExcel >= 0) {
            System.out.printf("Remark: You need to score %.2f on the Prelim Exam for an excellent grade.\n", 
                            requiredExamToExcel);
        }
        
        System.out.println("\n" + "=".repeat(60));
        
        scanner.close();
    }
}