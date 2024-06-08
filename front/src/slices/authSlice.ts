import { createSlice, createAsyncThunk, PayloadAction } from "@reduxjs/toolkit";
import axiosInstance from "../api/axiosInstance";
import { Role } from "../model/users/Role";
import { Examination } from "../model/examination/Examination";

type User = {
  email: string;
  password: string;
};

type NewUser = User & {
  id: number | null;
  ime: string;
  prezime: string;
  dateOfBirth: string;
  role: Role;
  bloodPressure: number;
  puls: number;
  saturationO2: number;
  bodyTemperature: number;
  examinations: Examination[];
};

type UserBasicInfo = {
  id: number;
  email: string;
  role: string;
  token: string;
};

type UserProfileData = {
  name: string;
  email: string;
};

type AuthApiState = {
  basicUserInfo?: UserBasicInfo | null;
  userProfileData?: UserProfileData | null;
  status: "idle" | "loading" | "failed";
  error: string | null;
};

const initialState: AuthApiState = {
  basicUserInfo: localStorage.getItem("userInfo")
    ? JSON.parse(localStorage.getItem("userInfo") as string)
    : null,
  userProfileData: undefined,
  status: "idle",
  error: null,
};

export const register = createAsyncThunk("patients", async (data: NewUser) => {
  const response = await axiosInstance.post("/api/patients", data);
  return response.data;
});

// Login action
export const login = createAsyncThunk("login", async (data: User) => {
  const response = await axiosInstance.post("/rest/auth/login", data);
  const resData: UserBasicInfo = response.data;
  localStorage.setItem("userInfo", JSON.stringify(resData));
  return resData;
});

// Logout action
export const logout = createAsyncThunk("logout", async () => {
  localStorage.removeItem("userInfo");
  delete axiosInstance.defaults.headers.common["Authorization"];
  return null;
});

// Get user action
export const getUser = createAsyncThunk(
  "users/profile",
  async (userId: string) => {
    const response = await axiosInstance.get(`/users/${userId}`);
    return response.data;
  }
);

const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(login.pending, (state) => {
        state.status = "loading";
        state.error = null;
      })
      .addCase(
        login.fulfilled,
        (state, action: PayloadAction<UserBasicInfo>) => {
          state.status = "idle";
          state.basicUserInfo = action.payload;
          axiosInstance.defaults.headers.common[
            "Authorization"
          ] = `Bearer ${action.payload.token}`;
        }
      )
      .addCase(login.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.error.message || "Login failed";
      })

      .addCase(register.pending, (state) => {
        state.status = "loading";
        state.error = null;
      })
      .addCase(register.fulfilled, (state, action) => {
        state.status = "idle";
      })
      .addCase(register.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.error.message || "Registration failed";
      })

      .addCase(logout.pending, (state) => {
        state.status = "loading";
        state.error = null;
      })
      .addCase(logout.fulfilled, (state) => {
        state.status = "idle";
        state.basicUserInfo = null;
      })
      .addCase(logout.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.error.message || "Logout failed";
      })

      .addCase(getUser.pending, (state) => {
        state.status = "loading";
        state.error = null;
      })
      .addCase(getUser.fulfilled, (state, action) => {
        state.status = "idle";
        state.userProfileData = action.payload;
      })
      .addCase(getUser.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.error.message || "Get user profile data failed";
      });
  },
});

export default authSlice.reducer;
