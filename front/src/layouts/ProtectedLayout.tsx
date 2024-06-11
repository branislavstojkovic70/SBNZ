// layouts/ProtectedLayout.tsx
import React, { useEffect } from "react";
import { Outlet, Navigate, useNavigate } from "react-router-dom";
import { useAppSelector } from "../hooks/redux-hooks";

const ProtectedLayout = () => {
  const basicUserInfo = useAppSelector((state) => state.auth.basicUserInfo);
  const navigate = useNavigate();

  useEffect(() => {
    if (basicUserInfo) {
      if (basicUserInfo.role === "Doctor") {
        navigate("/doctor");
      } else if (basicUserInfo.role === "Patient") {
        navigate("/patient");
      } else if (basicUserInfo.role === "Admin") {
        navigate("/admin");
      }
    } else {
      navigate("/login");
    }
  }, [basicUserInfo, navigate]);

  return <Outlet />;
};

export default ProtectedLayout;
