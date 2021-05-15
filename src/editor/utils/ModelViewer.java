package editor.utils;

import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES1;
import com.jogamp.opengl.GL2GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;

import editor.application.CacheEditor;
import editor.loaders.Model3D;

public class ModelViewer extends GLJPanel {
	
	private static final long serialVersionUID = 1L;
	private float pitch, yaw, roll, offset_y, offset_x, scale;
	private GLAutoDrawable gla;
	private GL2 gl;
	Point current, previous;
	int xdelta, ydelta, zdelta;
    boolean rightDown = false;
    boolean middleDown = false;
    boolean leftDown = false;
	boolean glControlLoaded = false;
    boolean modelLoaded = false;
	Model3D model;
	private int id;
	
	public ModelViewer() {
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
		@SuppressWarnings("unused")
		GLCapabilities capabilities = new GLCapabilities(profile);
    	this.scale = 0.52910346F;
        this.pitch = 190.0F;
        this.yaw = 23.0F;
        this.roll = 0.0F;
        this.offset_y = 6.0f;
        this.offset_x = 56.0f;
        this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(java.awt.event.MouseEvent arg0) {
				
			}

			@Override
			public void mouseEntered(java.awt.event.MouseEvent arg0) {
				
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent arg0) {
				
			}

			@Override
			public void mousePressed(java.awt.event.MouseEvent arg0) {
				if (arg0.getButton() == java.awt.event.MouseEvent.BUTTON1)
	                leftDown = true;
	            else if (arg0.getButton() == java.awt.event.MouseEvent.BUTTON2)
	                middleDown = true;
	            else if (arg0.getButton() == java.awt.event.MouseEvent.BUTTON3)
	                rightDown = true;
			}

			@Override
			public void mouseReleased(java.awt.event.MouseEvent arg0) {
				if (arg0.getButton() == java.awt.event.MouseEvent.BUTTON1)
					leftDown = false;
				else if (arg0.getButton() == java.awt.event.MouseEvent.BUTTON2)
					middleDown = false;
				else if (arg0.getButton() == java.awt.event.MouseEvent.BUTTON3)
					rightDown = false;
			}
			
		});
        this.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(java.awt.event.MouseEvent e) {
				UpdateMouse(e);
			}
			
			@Override
			public void mouseDragged(java.awt.event.MouseEvent e) {
				UpdateMouse(e);
				display();
			}
		});
        this.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				zdelta = (int) arg0.getPreciseWheelRotation();
				display();
			}
			
		});
        this.addGLEventListener(new GLEventListener() {

			@Override
            public void init(GLAutoDrawable glad) {

            }

            @Override
            public void dispose(GLAutoDrawable glad) {

            }
			@Override
            public void display(GLAutoDrawable glad) {
            	gla = glad;
                gl = glad.getGL().getGL2();
                if (!glControlLoaded) {
                	glControlLoaded = true;
                    SetupGL();
                    SetupViewport();
                }
                gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
            	int width = glad.getSurfaceWidth();
                int height = glad.getSurfaceHeight();
                gl.glViewport(0, 0, width, height);
                gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
                gl.glEnable(GL.GL_BLEND);
                gl.glLoadIdentity();
                float c = (float)Math.sqrt((double)(width * width) + (double)(height * height));
                gl.glOrtho(0.0F, (float)width, 0.0F, (float)height, -c, c);
                gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
                int a = 2;
                if (a == 3)  {
                	gl.glEnable(GLLightingFunc.GL_LIGHTING);
                	gl.glEnable(GLLightingFunc.GL_LIGHT0);
                	gl.glMaterialf(GL.GL_FRONT, GLLightingFunc.GL_SPECULAR, GLLightingFunc.GL_SPECULAR);
                	gl.glLightf(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_AMBIENT, GLLightingFunc.GL_AMBIENT);
                	gl.glLightf(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_DIFFUSE, GLLightingFunc.GL_DIFFUSE);
                	gl.glLightf(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_POSITION, GLLightingFunc.GL_POSITION);
                	gl.glMaterialf(GL.GL_FRONT, GLLightingFunc.GL_SHININESS, 50.0f);
                } else {
                	gl.glDisable(GLLightingFunc.GL_LIGHTING);
                	gl.glDisable(GLLightingFunc.GL_LIGHT0);
                }
                
                if ((leftDown && !rightDown && !middleDown)) {
                    yaw -= (float) xdelta / 2;
                    pitch += (float) ydelta / 2;
                }
                if (leftDown && rightDown || middleDown) {
                    offset_y -= (float) ydelta;
                    offset_x += (float) xdelta;
                }

                float wheel = (float)(zdelta / 10.0F) * -1.0F;
                if (wheel > 1.0F)
                    wheel = 1.0F;
                else if (wheel < -1.0F)
                    wheel = -1.0F;
                scale += scale * wheel;
                if (scale < 0.01F)
                    scale = 0.01F;
                zdelta = 0;
                if (modelLoaded) {
                    //if (mdl is Model3D) {
                        float x = ((float)-((100.0F * (scale))) + offset_x) + (float)width / 2.0F;
                        float y = ((float)-((100.0F * (scale))) + offset_y) + (float)(height - model.getHeight()) / 2.0F;
                        float z = 0.0F;
                        model.calcDimms(false);

                        model.render(x, y, z, pitch, yaw, roll, scale, scale, scale, gl);
                    /*} else if (mdl is ObjMesh) {
                        float x = (float)width / 2.0F;
                        float y = ((float)-((100.0F * (scale))) + offset_z) + (float)height / 2.0F;
                        float z = 0.0F;
                        gl.glLoadIdentity();
                        gl.glTranslatef(x, y, z);
                        gl.glRotatef(pitch, 1.0F, 0.0F, 0.0F);
                        gl.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
                        gl.glRotatef(roll, 0.0F, 0.0F, 1.0F);
                        gl.glScalef(scale, scale, scale);
                        
                   }*/
                   //DrawGrid(6, 100f);
                   drawXYZ();
                }
				glad.swapBuffers();
				//System.out.println(pitch + " " + yaw + " " + roll + " " + offset_y + " " + offset_x + " " + scale);
            }

            @Override
            public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {

            }
        });
	}
	
	private void UpdateMouse(java.awt.event.MouseEvent mouse) {
		current = mouse.getPoint();
        if (previous != null && (current.getX() != previous.getX() || current.getY() != previous.getY())) {
            xdelta = (int) (current.getX() - previous.getX());
            ydelta = (int) (current.getY() - previous.getY());
        } else if (current == previous) {
            xdelta = 0;
            ydelta = 0;
            zdelta = 0;
        }
        previous = current;
    }
	
	private void SetupGL()
    {
        gl.glClearColor(0.2f, 0.6f, 1.0f, 1.0f);
        gl.glShadeModel(GLLightingFunc.GL_SMOOTH);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glClearDepth(1.0);
        gl.glDepthFunc(GL.GL_LEQUAL);
        gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
        gl.glEnable(GLLightingFunc.GL_NORMALIZE);
        gl.glEnable(GL.GL_BLEND);
        //blending would be better but its hard to get alpha textures working
        //gl.glBlendFunc(BlendingFactorSrc.SrcAlpha, BlendingFactorDest.OneMinusSrcAlpha);
        gl.glEnable(GL2ES1.GL_POINT_SMOOTH);
        gl.glEnable(GL2GL3.GL_POLYGON_SMOOTH);
        gl.glEnable(GL.GL_LINE_SMOOTH);
        gl.glEnable(GLLightingFunc.GL_COLOR_MATERIAL);
        gl.glEnable(GL2ES1.GL_ALPHA_TEST);
        gl.glEnable(GL.GL_CULL_FACE);
        gl.glEnable(GLLightingFunc.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL.GL_FRONT, GLLightingFunc.GL_DIFFUSE);
        gl.glCullFace(GL.GL_BACK);
    }

	private void SetupViewport()//shouldnt need this
    {
        int w = gla.getSurfaceWidth();
        int h = gla.getSurfaceHeight();
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, w, 0, h, -1, 1);
        gl.glViewport(0, 0, w, h); 
    }
	
	void DrawGrid(int num, float size)
    {
		gl.glLineWidth(1);
		gl.glColor3f(0.9f, 0.9f, 0.9f);
		gl.glBegin(GL2.GL_LINES);
		for (int i = -num; i <= num; i++)
        {
            if (i != 0)
            {
                gl.glVertex3f(-num * size, 0, i * size);
                gl.glVertex3f(num * size, 0, i * size);
                gl.glVertex3f(i * size, 0, -num * size);
                gl.glVertex3f(i * size, 0, num * size);
            }
        }
		gl.glEnd();
		gl.glLineWidth(2);
		gl.glColor3f(0.0f, 0.0f, 0.0f);
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3f(0, 0, size * num);
        gl.glVertex3f(0, 0, -size * num);
        gl.glVertex3f(size * num, 0, 0);
        gl.glVertex3f(-size * num, 0, 0);
        gl.glEnd();
    }
	
	void drawXYZ()
     {
         float Ax = yaw;
         float Ay = pitch;
         
         gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
         gl.glLoadIdentity();
         gl.glTranslatef(30, 30, -30);
         gl.glRotatef(Ay, 1, 0, 0);
         gl.glRotatef(Ax, 0, 1, 0);
         gl.glLineWidth(1);
         gl.glBegin(GL.GL_LINES);
         gl.glColor3f(0.5f, 0, 0);
         gl.glVertex3f(10, 0, 0);
         gl.glVertex3f(20, 0, 0);
         gl.glEnd();
         gl.glBegin(GL.GL_LINES);
         gl.glColor3f(0, 0.5f, 0);
         gl.glVertex3f(0, 10f, 0);
         gl.glVertex3f(0, 20f, 0);
         gl.glEnd();
         gl.glBegin(GL.GL_LINES);
         gl.glColor3f(0, 0, 0.5f);
         gl.glVertex3f(0, 0, 10f);
         gl.glVertex3f(0, 0, 20f);
         gl.glEnd();
     }
	
	public void loadModel() {
		byte data[] = CacheEditor.STORE.getIndexes()[7].getFile(id, 0, null);
		model = new Model3D(data);
		modelLoaded = true;
		display();
		//m.decodeNew(data);
		/*for (int i = 0; i < model.vertexX.length; i++) {
			 System.out.println("X: " + model.vertexX[i] + ", Y: " + model.vertexY[i] + ", Z: " + model.vertexZ[i]);
		}*/
	}

	 // Example editable property.
	   private int numberOfSides = 3;

	    /**
	     * Get the value of numberOfSides
	     *
	     * @return the value of numberOfSides
	     */
	    public int getNumberOfSides() {
	        return numberOfSides;
	    }

	    /**
	     * Set the value of numberOfSides
	     *
	     * @param numberOfSides new value of numberOfSides
	     */
	    public void setNumberOfSides(int numberOfSides) {
	        this.numberOfSides = numberOfSides;
	    }
	    
	    public int getId() {
	    	return id;
	    }
	    
	    public void setId(int id) {
	    	this.id = id;
	    }
	
}
