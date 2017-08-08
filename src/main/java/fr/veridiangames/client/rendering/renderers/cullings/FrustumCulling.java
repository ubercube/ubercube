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

import fr.veridiangames.core.maths.Mathf;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.client.rendering.Camera;

public class FrustumCulling
{
    private Vec3 farTopLeft, farBottomRight, farTopRight, farBottomLeft;
    private Vec3 nearTopLeft, nearBottomRight, nearTopRight, nearBottomLeft;
    private Vec3 leftNormal, topNormal, rightNormal, bottomNormal;
    private float farHeight, farWidth, nearHeight, nearWidth;

    public void init(Camera camera)
    {
        farHeight = 2.0f * Mathf.tan(Mathf.toRadians(camera.getFov() / 2.0f)) * camera.getFar();
        farWidth = farHeight * camera.getAspect();

        nearHeight = 2.0f * Mathf.tan(Mathf.toRadians(camera.getFov() / 2.0f)) * camera.getNear();
        nearWidth = nearHeight * camera.getAspect();

        Vec3 camPos = camera.getTransform().getPosition();
        Vec3 camDir = camera.getTransform().getRotation().getForward().normalize();

        Vec3 camLeft = camera.getTransform().getRotation().getLeft().normalize();
        Vec3 camUp = camera.getTransform().getRotation().getUp().normalize();

        Vec3 farCenter = camPos.copy().add(camDir.copy().mul(camera.getFar()));
        farTopLeft = farCenter.copy().add(camLeft.copy().mul(farWidth / 2.0f)).add(camUp.copy().mul(farHeight / 2.0f));
        farBottomRight = farCenter.copy().add(camLeft.copy().mul(-farWidth / 2.0f)).add(camUp.copy().mul(-farHeight / 2.0f));
        farTopRight = farCenter.copy().add(camLeft.copy().mul(-farWidth / 2.0f)).add(camUp.copy().mul(farHeight / 2.0f));
        farBottomLeft = farCenter.copy().add(camLeft.copy().mul(farWidth / 2.0f)).add(camUp.copy().mul(-farHeight / 2.0f));

        Vec3 nearCenter = camPos.copy().add(camDir.copy().mul(camera.getNear()));
        nearTopLeft = nearCenter.copy().add(camLeft.copy().mul(nearWidth / 2.0f)).add(camUp.copy().mul(nearHeight / 2.0f));
        nearBottomRight = nearCenter.copy().add(camLeft.copy().mul(-nearWidth / 2.0f)).add(camUp.copy().mul(-nearHeight / 2.0f));
        nearTopRight = nearCenter.copy().add(camLeft.copy().mul(-nearWidth / 2.0f)).add(camUp.copy().mul(nearHeight / 2.0f));
        nearBottomLeft = nearCenter.copy().add(camLeft.copy().mul(nearWidth / 2.0f)).add(camUp.copy().mul(-nearHeight / 2.0f));

        Vec3 topLeft = farTopLeft.copy().sub(nearTopLeft);
        Vec3 bottomLeft = farBottomLeft.copy().sub(nearBottomLeft);
        Vec3 bottomRight = farBottomRight.copy().sub(nearBottomRight);
        Vec3 topRight = farTopRight.copy().sub(nearTopRight);

        leftNormal = nearTopLeft.copy().sub(nearBottomLeft).cross(bottomLeft).normalize();
        topNormal = nearTopRight.copy().sub(nearTopLeft).cross(topLeft).normalize();
        rightNormal = nearBottomRight.copy().sub(nearTopRight).cross(topRight).normalize();
        bottomNormal = nearBottomLeft.copy().sub(nearBottomRight).cross(bottomRight).normalize();
    }

    public void update(Camera camera)
    {
        Vec3 camPos = camera.getTransform().getPosition();
        Vec3 camDir = camera.getTransform().getRotation().getForward().normalize();

        Vec3 camLeft = camera.getTransform().getRotation().getLeft().normalize();
        Vec3 camUp = camera.getTransform().getRotation().getUp().normalize();

        Vec3 farCenter = camPos.copy().add(camDir.copy().mul(camera.getFar()));
        farTopLeft = farCenter.copy().add(camLeft.copy().mul(farWidth / 2.0f)).add(camUp.copy().mul(farHeight / 2.0f));
        farBottomRight = farCenter.copy().add(camLeft.copy().mul(-farWidth / 2.0f)).add(camUp.copy().mul(-farHeight / 2.0f));
        farTopRight = farCenter.copy().add(camLeft.copy().mul(-farWidth / 2.0f)).add(camUp.copy().mul(farHeight / 2.0f));
        farBottomLeft = farCenter.copy().add(camLeft.copy().mul(farWidth / 2.0f)).add(camUp.copy().mul(-farHeight / 2.0f));

        Vec3 nearCenter = camPos.copy().add(camDir.copy().mul(camera.getNear()));
        nearTopLeft = nearCenter.copy().add(camLeft.copy().mul(nearWidth / 2.0f)).add(camUp.copy().mul(nearHeight / 2.0f));
        nearBottomRight = nearCenter.copy().add(camLeft.copy().mul(-nearWidth / 2.0f)).add(camUp.copy().mul(-nearHeight / 2.0f));
        nearTopRight = nearCenter.copy().add(camLeft.copy().mul(-nearWidth / 2.0f)).add(camUp.copy().mul(nearHeight / 2.0f));
        nearBottomLeft = nearCenter.copy().add(camLeft.copy().mul(nearWidth / 2.0f)).add(camUp.copy().mul(-nearHeight / 2.0f));

        Vec3 topLeft = farTopLeft.copy().sub(nearTopLeft);
        Vec3 bottomLeft = farBottomLeft.copy().sub(nearBottomLeft);
        Vec3 bottomRight = farBottomRight.copy().sub(nearBottomRight);
        Vec3 topRight = farTopRight.copy().sub(nearTopRight);

        leftNormal = nearTopLeft.copy().sub(nearBottomLeft).cross(bottomLeft).normalize();
        topNormal = nearTopRight.copy().sub(nearTopLeft).cross(topLeft).normalize();
        rightNormal = nearBottomRight.copy().sub(nearTopRight).cross(topRight).normalize();
        bottomNormal = nearBottomLeft.copy().sub(nearBottomRight).cross(bottomRight).normalize();
    }

    public boolean isInViewFrustum(Vec3 pos, float radius)
    {
        if (farTopLeft == null) return true;

        if (leftNormal.dot(pos) + leftNormal.copy().negate().dot(nearBottomLeft) + radius < 0) return false;
        if (rightNormal.dot(pos) + rightNormal.copy().negate().dot(nearTopRight) + radius < 0) return false;
        if (topNormal.dot(pos) + topNormal.copy().negate().dot(nearTopLeft) + radius < 0) return false;
        if (bottomNormal.dot(pos) + bottomNormal.copy().negate().dot(nearBottomRight) + radius < 0) return false;

        return true;
    }
}