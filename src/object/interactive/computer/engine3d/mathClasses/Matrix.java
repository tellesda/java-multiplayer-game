package object.interactive.computer.engine3d.mathClasses;

public class Matrix {

    //Attributes
    private final int m;
    private final int n;
    private Float[][] matrix;

    //Constructor
    public Matrix(int m, int n){
        this.m = m;
        this.n = n;
        matrix = new Float[m][n];
    }

    //Getters
    public int getM() { return m; }
    public int getN() { return n; }
    public Float[][] getMatrix() { return matrix; }
    public Float getElement(int m, int n){ return matrix[m][n]; }
    public Matrix getSubMatrix(int m, int n){

        Matrix result = new Matrix(getM() - 1, getN() - 1);

        int indexM = 0;
        for(int i = 0; i < getM(); i++){

            if(i == m){
                continue;
            }

            int indexN = 0;
            for(int j = 0; j < getN(); j++){
                if(j != n){
                    result.set(indexM, indexN, getElement(i, j));
                    indexN++;
                }
            }
            indexM++;
        }
        return result;
    }

    //Setters
    public void set(int m, int n, Float value){
        matrix[m][n] = value;
    }
    public void setMatrix(Float[][] matrix){
        this.matrix = matrix;
    }

    //Class methods
    public static Matrix scale(Matrix matrix, float scalar){

        Matrix result = new Matrix(matrix.getM(), matrix.getN());

        for(int i=0; i< matrix.getM(); i++){
            for(int j=0; j<matrix.getN(); j++){
                float value = matrix.getElement(i, j) * scalar;
                result.set(i, j, value);
            }
        }
        return result;
    }

    public static Matrix add(Matrix m1, Matrix m2){

        if(m1.getM() != m2.getM() || m1.getN() != m2.getN()){
            System.err.println("Error : Incompatible matrices for addition");
        }

        Matrix result = new Matrix(m1.getM(), m1.getN());

        for(int i = 0; i < result.getM(); i++){
            for (int j = 0; j < result.getN(); j++){
                float value = m1.getElement(i, j) + m2.getElement(i, j);
                result.set(i, j, value);
            }
        }
        return result;
    }

    public static Matrix sub(Matrix m1, Matrix m2){

        if(m1.getM() != m2.getM() || m1.getN() != m2.getN()){
            System.err.println("Error : Incompatible matrices for subtraction");
        }

        Matrix result = new Matrix(m1.getM(), m1.getN());

        for(int col = 0; col < result.getM(); col++){
            for (int row = 0; row < result.getN(); row++){
                float value = m1.getElement(col, row) - m2.getElement(col, row);
                result.set(col, row, value);
            }
        }
        return result;
    }

    public static Matrix mul(Matrix m1, Matrix m2){

        Matrix result = new Matrix(m1.getM(), m2.getN());

        for(int col = 0; col < result.getM(); col++){
            for(int row = 0; row < result.getN(); row++){

                float value = 0;

                for(int mult = 0; mult < m1.getN(); mult ++){
                    value += (m1.getElement(col, mult) * m2.getElement(mult, row));
                }

                result.set(col, row, value);
            }
        }

        return result;
    }

    //Specific for rendering
    public static Vector3d fastMul(Matrix m4x4, Vector3d vector){
        return new Vector3d(
                m4x4.getElement(0,0)* vector.getX() +
                m4x4.getElement(0,1)* vector.getY() +
                m4x4.getElement(0,2)* vector.getZ() +
                m4x4.getElement(0,3),

                m4x4.getElement(1,0)*vector.getX() +
                        m4x4.getElement(1,1)*vector.getY() +
                        m4x4.getElement(1,2)*vector.getZ() +
                        m4x4.getElement(1,3),

                m4x4.getElement(2,0)*vector.getX() +
                        m4x4.getElement(2,1)*vector.getY() +
                        m4x4.getElement(2,2)*vector.getZ() +
                        m4x4.getElement(2,3));
    }

    public static Matrix fastMul2(Matrix m4x4, Matrix m4x1){
        Matrix result = new Matrix(4,1);

        result.set(0,0, m4x4.getElement(0,0)* m4x1.getElement(0,0) +
                                    m4x4.getElement(0,1)* m4x1.getElement(1,0) +
                                    m4x4.getElement(0,2)* m4x1.getElement(2,0) +
                                    m4x4.getElement(0,3)* m4x1.getElement(3,0));

        result.set(1,0, m4x4.getElement(1,0)* m4x1.getElement(0,0) +
                m4x4.getElement(1,1)* m4x1.getElement(1,0) +
                m4x4.getElement(1,2)* m4x1.getElement(2,0) +
                m4x4.getElement(1,3)* m4x1.getElement(3,0));

        result.set(2,0, m4x4.getElement(2,0)* m4x1.getElement(0,0) +
                m4x4.getElement(2,1)* m4x1.getElement(1,0) +
                m4x4.getElement(2,2)* m4x1.getElement(2,0) +
                m4x4.getElement(2,3)* m4x1.getElement(3,0));

        result.set(3,0, m4x4.getElement(3,0)* m4x1.getElement(0,0) +
                m4x4.getElement(3,1)* m4x1.getElement(1,0) +
                m4x4.getElement(3,2)* m4x1.getElement(2,0) +
                m4x4.getElement(3,3)* m4x1.getElement(3,0));
        return result;
    }

    public static float det(Matrix matrix, int row){

        if(matrix.getM() == 2){
            return (matrix.getElement(0,0) * matrix.getElement(1,1) - matrix.getElement(0,1) * matrix.getElement(1,0));
        }

        float result = 0;
        for(int col = 0; col < matrix.getN(); col++){

            float elementIJ = matrix.getElement(0, col);

            if(elementIJ != 0){
                int coefficient;
                if((col+row) % 2 == 0){ coefficient = 1;} else { coefficient = -1;}
                result += coefficient * elementIJ * det(matrix.getSubMatrix(0, col), row);
            }
        }
        return result;
    }

    public static Matrix invert(Matrix matrix){

        float det = det(matrix, 0);

        if(matrix.getM() == 2){
            Matrix matrix2 = new Matrix(2,2);
            matrix2.set(0,0, matrix.getElement(1,1));
            matrix2.set(1,1, matrix.getElement(0,0));
            matrix2.set(0,1, matrix.getElement(0,1) * -1);
            matrix2.set(1,0, matrix.getElement(1,0) * -1);
            return scale(matrix2, (1/det));
        }

        Matrix result = new Matrix(matrix.getM(), matrix.getN());

        for(int i=0; i<matrix.getM(); i++){
            for(int j=0; j<matrix.getN(); j++){
                float value = det(matrix.getSubMatrix(i,j), i);
                result.set(i,j,value);
            }
        }
        return scale(Matrix.transpose(result), (1/det));
    }

    public static Matrix transpose(Matrix matrix){

        Matrix result = new Matrix(matrix.getN(), matrix.getM());

        for(int i=0; i<matrix.getM(); i++){
            for(int j=0; j<matrix.getN(); j++){
                result.set(j,i, matrix.getElement(i,j));
            }
        }
        return result;
    }

    public static Matrix getRotXMatrix(float angle){
        Matrix rotX = new Matrix(4,4);

        float cos = (float)Math.cos(angle);
        float sin = (float)Math.sin(angle);

        rotX.setMatrix(new Float[][] {{1f,0f,0f,0f},
                                      {0f,cos, -1*sin,0f},
                                      {0f,sin,cos,0f},
                                      {0f,0f,0f,1f}});
        return rotX;
    }

    public static Matrix getRotYMatrix(float angle){
        Matrix rotY = new Matrix(4,4);

        float cos = (float)Math.cos(angle);
        float sin = (float)Math.sin(angle);

        rotY.setMatrix(new Float[][] {{cos,0f,-1*sin,0f},
                                      {0f,1f,0f,0f},
                                      {sin,0f,cos,0f},
                                      {0f,0f,0f,1f}});
        return rotY;
    }

    public static Matrix getRotZMatrix(float angle){
        Matrix rotZ = new Matrix(4,4);

        float cos = (float)Math.cos(angle);
        float sin = (float)Math.sin(angle);

        rotZ.setMatrix(new Float[][] {{cos,-1*sin,0f,0f},
                                      {sin,cos,0f,0f},
                                      {0f,0f,1f,0f},
                                      {0f,0f,0f,1f}});
        return rotZ;
    }

    public static Matrix getRotMatrix(Rotator3d rotator){
        return mul(getRotXMatrix(rotator.getX()), mul(getRotYMatrix(rotator.getY()), getRotZMatrix(rotator.getZ())));
    }

    public static Matrix getTranslationMatrix(Vector3d location){
        Matrix translation = new Matrix(4,4);
        translation.setMatrix(new Float[][] {{1f,0f,0f, location.getX()},
                                             {0f,1f,0f, location.getY()},
                                             {0f,0f,1f, location.getZ()},
                                             {0f,0f,0f,1f}});
        return translation;
    }

    public static Matrix getScaleMatrix(Vector3d scale){
        Matrix translation = new Matrix(4,4);
        translation.setMatrix(new Float[][] {{scale.getX(),0f,0f,0f},
                                             {0f, scale.getY(),0f,0f},
                                             {0f,0f, scale.getZ(),0f},
                                             {0f,0f,0f,1f}});
        return translation;
    }

    public static Matrix getTransformMatrix(Vector3d location, Rotator3d rotation, Vector3d scale){

        Matrix translateM = getTranslationMatrix(location);
        Matrix rotateM = getRotMatrix(rotation);
        Matrix scaleM = getScaleMatrix(scale);

        return Matrix.mul(translateM, Matrix.mul(rotateM, scaleM));

    }

    public static Matrix getProjectionMatrix(float verticalFov,float horizontalFov ,float zFar, float zNear){
        Matrix result = new Matrix(4,4);

        float verticalFovRad = verticalFov*3.14159f/180.0f;
        float horizontalFovRad = horizontalFov*(3.14159f/180.0f);

        float cot1 = 1.0f / (float)Math.tan(0.5f * horizontalFovRad);
        float cot2 = 1.0f / (float)Math.tan(0.5f * verticalFovRad);

        float eq1 = (zFar + zNear)/(zNear - zFar);
        float eq2 = 2*zFar*zNear/(zNear - zFar);

        result.setMatrix(new Float[][] {{cot1,0f,0f,0f},
                                        {0f,cot2,0f,0f},
                                        {0f,0f,eq1,eq2},
                                        {0f,0f,1f,0f}});
        return result;
    }

    public static Vector3d applyPerspective(Matrix m){
        float wInv = 1/m.getElement(3,0);

        return new Vector3d(
                m.getElement(0,0) * wInv,
                m.getElement(1,0) * wInv,
                m.getElement(2,0) * wInv);
    }

    //TODO correct scale for future uses
    public static Matrix extractRotation(Matrix transformMatrix){
        transformMatrix.set(0,3, 0f);
        transformMatrix.set(1,3, 0f);
        transformMatrix.set(2,3, 0f);
        return transformMatrix;
    }

    public static void print(Matrix matrix){
        for(int col = 0; col < matrix.getM(); col++){
            for(int row = 0; row < matrix.getN(); row++){
                System.out.print(matrix.getElement(col, row) + ", ");
            }
            System.out.println();
        }
    }
}
