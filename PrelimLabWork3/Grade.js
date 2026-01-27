// Node.js Console-based Implementation
const readline = require('readline');

const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

// Constants for grading breakdown
const ATTENDANCE_WEIGHT = 0.40;
const LABWORK_WEIGHT = 0.60;
const CLASS_STANDING_WEIGHT = 0.70;
const PRELIM_EXAM_WEIGHT = 0.30;
const PASSING_GRADE = 75.0;
const EXCELLENT_GRADE = 100.0;
const TOTAL_CLASSES = 12; // Assuming 12 total classes

console.log("=".repeat(60));
console.log("         PRELIM GRADE CALCULATOR");
console.log("=".repeat(60));

// Function to ask questions sequentially
function askQuestion(query) {
    return new Promise(resolve => rl.question(query, resolve));
}

async function calculatePrelimGrade() {
    try {
        // Input: Number of attendances
        const attendances = parseFloat(await askQuestion("\nEnter number of attendances: "));
        
        // Input: Lab Work grades
        const labWork1 = parseFloat(await askQuestion("Enter Lab Work 1 grade: "));
        const labWork2 = parseFloat(await askQuestion("Enter Lab Work 2 grade: "));
        const labWork3 = parseFloat(await askQuestion("Enter Lab Work 3 grade: "));
        
        // Validate inputs
        if (isNaN(attendances) || attendances < 0 || attendances > TOTAL_CLASSES) {
            console.log("\nError: Invalid number of attendances!");
            rl.close();
            return;
        }
        
        if (isNaN(labWork1) || labWork1 < 0 || labWork1 > 100 ||
            isNaN(labWork2) || labWork2 < 0 || labWork2 > 100 ||
            isNaN(labWork3) || labWork3 < 0 || labWork3 > 100) {
            console.log("\nError: Lab work grades must be between 0 and 100!");
            rl.close();
            return;
        }
        
        // Formula 1: Lab Work Average
        const labWorkAverage = (labWork1 + labWork2 + labWork3) / 3.0;
        
        // Calculate Attendance Score (out of 100)
        const attendanceScore = (attendances / TOTAL_CLASSES) * 100.0;
        
        // Formula 2: Class Standing
        const classStanding = (ATTENDANCE_WEIGHT * attendanceScore) + 
                              (LABWORK_WEIGHT * labWorkAverage);
        
        // Formula 3: Calculate required Prelim Exam score
        // Prelim Grade = (0.30 × Prelim Exam) + (0.70 × Class Standing)
        // Solving for Prelim Exam:
        // Prelim Exam = (Prelim Grade - 0.70 × Class Standing) / 0.30
        
        const requiredExamToPass = (PASSING_GRADE - (CLASS_STANDING_WEIGHT * classStanding)) 
                                   / PRELIM_EXAM_WEIGHT;
        
        const requiredExamToExcel = (EXCELLENT_GRADE - (CLASS_STANDING_WEIGHT * classStanding)) 
                                    / PRELIM_EXAM_WEIGHT;
        
        // Display Results
        console.log("\n" + "=".repeat(60));
        console.log("                    RESULTS");
        console.log("=".repeat(60));
        
        console.log(`\nAttendance Score:          ${attendanceScore.toFixed(2)}% (${attendances} out of ${TOTAL_CLASSES} classes)`);
        console.log(`Lab Work 1:                ${labWork1.toFixed(2)}`);
        console.log(`Lab Work 2:                ${labWork2.toFixed(2)}`);
        console.log(`Lab Work 3:                ${labWork3.toFixed(2)}`);
        console.log(`Lab Work Average:          ${labWorkAverage.toFixed(2)}`);
        console.log(`Class Standing:            ${classStanding.toFixed(2)}`);
        
        console.log("\n" + "-".repeat(60));
        console.log("         REQUIRED PRELIM EXAM SCORES");
        console.log("-".repeat(60));
        
        // Display required exam score to pass
        console.log(`\nTo PASS (75%):             ${requiredExamToPass.toFixed(2)}`);
        
        if (requiredExamToPass > 100) {
            console.log("Remark: It is mathematically impossible to pass.");
            console.log("        Your current class standing is too low.");
        } else if (requiredExamToPass < 0) {
            console.log("Remark: You will pass regardless of your Prelim Exam score!");
        } else {
            console.log(`Remark: You need to score at least ${requiredExamToPass.toFixed(2)} on the Prelim Exam to pass.`);
        }
        
        // Display required exam score for excellent grade
        console.log(`\nTo achieve EXCELLENT (100%): ${requiredExamToExcel.toFixed(2)}`);
        
        if (requiredExamToExcel > 100) {
            console.log("Remark: It is mathematically impossible to achieve an excellent grade.");
        } else if (requiredExamToExcel <= 100 && requiredExamToExcel >= 0) {
            console.log(`Remark: You need to score ${requiredExamToExcel.toFixed(2)} on the Prelim Exam for an excellent grade.`);
        }
        
        console.log("\n" + "=".repeat(60));
        
    } catch (error) {
        console.log("\nError occurred:", error.message);
    } finally {
        rl.close();
    }
}

// Run the calculator
calculatePrelimGrade();