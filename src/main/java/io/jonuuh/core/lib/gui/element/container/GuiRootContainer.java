//    @Override
//    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
//    {
////        GL11.glPushMatrix();
////        GL11.glEnable(GL11.GL_SCISSOR_TEST);
////        RenderUtils.scissorFromTopLeft(xPos, yPos, width, height);
//
//        super.onScreenDraw(mouseX, mouseY, partialTicks);
//
////        GL11.glDisable(GL11.GL_SCISSOR_TEST);
////        GL11.glPopMatrix();
//        childrenMap.values().forEach(element -> element.onScreenDraw(mouseX, mouseY, partialTicks));
//
////        for (GuiElement element : getNestedChildren())
////        {
////            if (!(element instanceof GuiContainer))
////            {
////                element.onScreenDraw(mouseX, mouseY, partialTicks);
////            }
////            else
////            {
////                super.onScreenDraw(mouseX, mouseY, partialTicks);
//////                System.out.println(element.elementName + " " + element.getNumParents());
////                if (element.getNumParents() == 0)
////                {
////                    super.onScreenDraw(mouseX, mouseY, partialTicks);
////
//////                    element.onScreenDraw(mouseX, mouseY, partialTicks);
////                }
////            }
////        }
//    }

