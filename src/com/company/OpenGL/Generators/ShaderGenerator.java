package  com.company.OpenGL.Generators;

import com.company.Utils.BicycleDebugger;

import static org.lwjgl.opengl.GL20.*;

public class ShaderGenerator {
    private static final String TAG = "ShaderHelper";
    public static int compileVertexShader(String shaderCode) {
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }
    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int compileShader(int type, String shaderCode){
        final int shaderObjectId = glCreateShader(type);

        if (shaderObjectId == 0) {
            BicycleDebugger.w(TAG, "Could not create shader.");
        }
        glShaderSource(shaderObjectId, shaderCode);
        glCompileShader(shaderObjectId);

        int compileStatus = glGetShaderi(shaderObjectId, GL_COMPILE_STATUS);

        if (compileStatus == 0) {
            //Выводим в лог информацию о компиляции
            BicycleDebugger.v(TAG, "Results of compiling source:\n" + shaderCode + '\n');
            BicycleDebugger.e(TAG, glGetShaderInfoLog(shaderObjectId));
            glDeleteShader(shaderObjectId);
            BicycleDebugger.w(TAG, "Compilation of shader failed.");
            return 0;
        }
        return shaderObjectId;
    }

    public static int linkProgram(int vertexShaderId, int fragmentShaderId, boolean deleteShaders) {
        final int programObjectId = glCreateProgram();

        if (programObjectId==0) {
            BicycleDebugger.w(TAG, "Could not create new program.");
            return 0;
        }

        glAttachShader(programObjectId, vertexShaderId);
        glAttachShader(programObjectId, fragmentShaderId);
        glLinkProgram(programObjectId);
        int linkStatus = glGetProgrami(programObjectId, GL_LINK_STATUS);

        if (linkStatus == 0){
            BicycleDebugger.v(TAG, "Results of linking program:\n" );
            BicycleDebugger.e(TAG, glGetProgramInfoLog(programObjectId));
            glDeleteProgram(programObjectId);
            BicycleDebugger.w(TAG, "Linking of program failed.");
            return 0;
        }

        if (deleteShaders) {
            glDeleteShader(vertexShaderId);
            glDeleteShader(fragmentShaderId);
        }

        return programObjectId;
    }

    public static int createProgram(String vertexPath, String fragmentPath) {
        return createProgramFromString(TextResourceReader.readTextFile(vertexPath),TextResourceReader.readTextFile(fragmentPath));
    }
    public static int createProgramFromString(String vertexShader, String fragmentShader)
    {
        return linkProgram(
                compileVertexShader(vertexShader),
                compileFragmentShader(fragmentShader),
                true);
    }

    public static boolean validateProgram(int programObjectId) {
        glValidateProgram(programObjectId);
        int validateStatus = glGetProgrami(programObjectId, GL_VALIDATE_STATUS);
        BicycleDebugger.v(TAG, "Results of validating:\n" + validateStatus + "\nLog" +
                glGetProgramInfoLog(programObjectId));
        return validateStatus!=0;
    }
}
