package rendering;

import math.Vector2D;

public interface LitObject {

    void updateLightTexture(ShadowMap shadowMap);

    Vector2D getLocation();
}
