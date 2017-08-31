/*
 * Copyright (C) 2016 Team Ubercube
 *
 * This file is part of Ubercube.
 *
 *     Ubercube is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Ubercube is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Ubercube.  If not, see http://www.gnu.org/licenses/.
 */

package fr.veridiangames.client.rendering.renderers.cullings;

import fr.veridiangames.client.rendering.Camera;
import fr.veridiangames.core.maths.Mathf;
import fr.veridiangames.core.maths.Vec3;

public class FrustumCulling
{
    private Vec3 farTopLeft, farBottomRight, farTopRight, farBottomLeft;
    private Vec3 nearTopLeft, nearBottomRight, nearTopRight, nearBottomLeft;
    private Vec3 leftNormal, topNormal, rightNormal, bottomNormal;
    private float farHeight, farWidth, nearHeight, nearWidth;

    public void init(Camera camera)
    {
        this.farHeight = 2.0f * Mathf.tan(Mathf.toRadians(camera.getFov() / 2.0f)) * camera.getFar();
        this.farWidth = this.farHeight * camera.getAspect();

        this.nearHeight = 2.0f * Mathf.tan(Mathf.toRadians(camera.getFov() / 2.0f)) * camera.getNear();
        this.nearWidth = this.nearHeight * camera.getAspect();

        Vec3 camPos = camera.getTransform().getPosition();
        Vec3 camDir = camera.getTransform().getRotation().getForward().normalize();

        Vec3 camLeft = camera.getTransform().getRotation().getLeft().normalize();
        Vec3 camUp = camera.getTransform().getRotation().getUp().normalize();

        Vec3 farCenter = camPos.copy().add(camDir.copy().mul(camera.getFar()));
        this.farTopLeft = farCenter.copy().add(camLeft.copy().mul(this.farWidth / 2.0f)).add(camUp.copy().mul(this.farHeight / 2.0f));
        this.farBottomRight = farCenter.copy().add(camLeft.copy().mul(-this.farWidth / 2.0f)).add(camUp.copy().mul(-this.farHeight / 2.0f));
        this.farTopRight = farCenter.copy().add(camLeft.copy().mul(-this.farWidth / 2.0f)).add(camUp.copy().mul(this.farHeight / 2.0f));
        this.farBottomLeft = farCenter.copy().add(camLeft.copy().mul(this.farWidth / 2.0f)).add(camUp.copy().mul(-this.farHeight / 2.0f));

        Vec3 nearCenter = camPos.copy().add(camDir.copy().mul(camera.getNear()));
        this.nearTopLeft = nearCenter.copy().add(camLeft.copy().mul(this.nearWidth / 2.0f)).add(camUp.copy().mul(this.nearHeight / 2.0f));
        this.nearBottomRight = nearCenter.copy().add(camLeft.copy().mul(-this.nearWidth / 2.0f)).add(camUp.copy().mul(-this.nearHeight / 2.0f));
        this.nearTopRight = nearCenter.copy().add(camLeft.copy().mul(-this.nearWidth / 2.0f)).add(camUp.copy().mul(this.nearHeight / 2.0f));
        this.nearBottomLeft = nearCenter.copy().add(camLeft.copy().mul(this.nearWidth / 2.0f)).add(camUp.copy().mul(-this.nearHeight / 2.0f));

        Vec3 topLeft = this.farTopLeft.copy().sub(this.nearTopLeft);
        Vec3 bottomLeft = this.farBottomLeft.copy().sub(this.nearBottomLeft);
        Vec3 bottomRight = this.farBottomRight.copy().sub(this.nearBottomRight);
        Vec3 topRight = this.farTopRight.copy().sub(this.nearTopRight);

        this.leftNormal = this.nearTopLeft.copy().sub(this.nearBottomLeft).cross(bottomLeft).normalize();
        this.topNormal = this.nearTopRight.copy().sub(this.nearTopLeft).cross(topLeft).normalize();
        this.rightNormal = this.nearBottomRight.copy().sub(this.nearTopRight).cross(topRight).normalize();
        this.bottomNormal = this.nearBottomLeft.copy().sub(this.nearBottomRight).cross(bottomRight).normalize();
    }

    public void update(Camera camera)
    {
        Vec3 camPos = camera.getTransform().getPosition();
        Vec3 camDir = camera.getTransform().getRotation().getForward().normalize();

        Vec3 camLeft = camera.getTransform().getRotation().getLeft().normalize();
        Vec3 camUp = camera.getTransform().getRotation().getUp().normalize();

        Vec3 farCenter = camPos.copy().add(camDir.copy().mul(camera.getFar()));
        this.farTopLeft = farCenter.copy().add(camLeft.copy().mul(this.farWidth / 2.0f)).add(camUp.copy().mul(this.farHeight / 2.0f));
        this.farBottomRight = farCenter.copy().add(camLeft.copy().mul(-this.farWidth / 2.0f)).add(camUp.copy().mul(-this.farHeight / 2.0f));
        this.farTopRight = farCenter.copy().add(camLeft.copy().mul(-this.farWidth / 2.0f)).add(camUp.copy().mul(this.farHeight / 2.0f));
        this.farBottomLeft = farCenter.copy().add(camLeft.copy().mul(this.farWidth / 2.0f)).add(camUp.copy().mul(-this.farHeight / 2.0f));

        Vec3 nearCenter = camPos.copy().add(camDir.copy().mul(camera.getNear()));
        this.nearTopLeft = nearCenter.copy().add(camLeft.copy().mul(this.nearWidth / 2.0f)).add(camUp.copy().mul(this.nearHeight / 2.0f));
        this.nearBottomRight = nearCenter.copy().add(camLeft.copy().mul(-this.nearWidth / 2.0f)).add(camUp.copy().mul(-this.nearHeight / 2.0f));
        this.nearTopRight = nearCenter.copy().add(camLeft.copy().mul(-this.nearWidth / 2.0f)).add(camUp.copy().mul(this.nearHeight / 2.0f));
        this.nearBottomLeft = nearCenter.copy().add(camLeft.copy().mul(this.nearWidth / 2.0f)).add(camUp.copy().mul(-this.nearHeight / 2.0f));

        Vec3 topLeft = this.farTopLeft.copy().sub(this.nearTopLeft);
        Vec3 bottomLeft = this.farBottomLeft.copy().sub(this.nearBottomLeft);
        Vec3 bottomRight = this.farBottomRight.copy().sub(this.nearBottomRight);
        Vec3 topRight = this.farTopRight.copy().sub(this.nearTopRight);

        this.leftNormal = this.nearTopLeft.copy().sub(this.nearBottomLeft).cross(bottomLeft).normalize();
        this.topNormal = this.nearTopRight.copy().sub(this.nearTopLeft).cross(topLeft).normalize();
        this.rightNormal = this.nearBottomRight.copy().sub(this.nearTopRight).cross(topRight).normalize();
        this.bottomNormal = this.nearBottomLeft.copy().sub(this.nearBottomRight).cross(bottomRight).normalize();
    }

    public boolean isInViewFrustum(Vec3 pos, float radius)
    {
        if (this.farTopLeft == null) return true;

        if (this.leftNormal.dot(pos) + this.leftNormal.copy().negate().dot(this.nearBottomLeft) + radius < 0) return false;
        if (this.rightNormal.dot(pos) + this.rightNormal.copy().negate().dot(this.nearTopRight) + radius < 0) return false;
        if (this.topNormal.dot(pos) + this.topNormal.copy().negate().dot(this.nearTopLeft) + radius < 0) return false;
        if (this.bottomNormal.dot(pos) + this.bottomNormal.copy().negate().dot(this.nearBottomRight) + radius < 0) return false;

        return true;
    }
}