#version 330
// Note: There is no need to modify anything in this src file

in vec2 vVertex; //object space vertex position

//output to fragment shader
out vec2 vUV;					
 
void main()
{  
	//set the current object space position as the output texture coordinates
	//and the clip space position
 	vUV = vVertex;	
	gl_Position = vec4(vVertex.xy ,0,1);
}


