import React, { useEffect } from "react";
import { Button } from "@mui/material";
import { useAppDispatch, useAppSelector } from "../hooks/redux-hooks";
import { getUser, logout } from "../slices/authSlice";
import { useNavigate } from "react-router-dom";

const Home = () => {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const basicUserInfo = useAppSelector((state) => state.auth.basicUserInfo);

  useEffect(() => {
    if (basicUserInfo && basicUserInfo.id !== undefined) {
      console.log(basicUserInfo)
    }
  }, [basicUserInfo]);

  const handleLogout = async () => {
    try {
      await dispatch(logout()).unwrap();
      navigate("/login");
    } catch (e) {
      console.error(e);
    }
  };

  return (
    <>
      <h1>Home</h1>
      <h4>Id: {basicUserInfo?.id}</h4>
      <h4>Email: {basicUserInfo?.email}</h4>
      <Button variant="contained" sx={{ mt: 3, mb: 2 }} onClick={handleLogout}>
        Logout
      </Button>
    </>
  );
};

export default Home;